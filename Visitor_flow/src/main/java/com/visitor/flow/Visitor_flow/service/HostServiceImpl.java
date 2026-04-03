package com.visitor.flow.Visitor_flow.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.repository.VisitorRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class HostServiceImpl implements HostService {

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public List<VisitorImfo> getVisitorsByDepartment(String department) {
        return visitorRepository.findAll().stream()
            .filter(v -> department.equalsIgnoreCase(v.getDepartment()))
            .collect(Collectors.toList());
    }

    @Override
    public List<VisitorImfo> getPendingByDepartment(String department) {
        return visitorRepository.findAll().stream()
            .filter(v -> department.equalsIgnoreCase(v.getDepartment())
                      && "PENDING".equalsIgnoreCase(v.getStatus()))
            .collect(Collectors.toList());
    }

    // ── Approve + QR email ──
    @Override
    public VisitorImfo approveByHost(Long id, String hostEmail) {
        VisitorImfo visitor = visitorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visitor not found: " + id));

        if (!"PENDING".equalsIgnoreCase(visitor.getStatus())) {
            throw new RuntimeException("Visitor already " + visitor.getStatus());
        }

        visitor.setStatus("APPROVED");
        VisitorImfo saved = visitorRepository.save(visitor);

        if (visitor.getEmail() != null && !visitor.getEmail().isBlank()) {
            try {
                sendQrPassEmail(saved);
            } catch (Exception e) {
                System.err.println("Email failed for visitor " + id + ": " + e.getMessage());
            }
        }
        return saved;
    }

    @Override
    public VisitorImfo rejectByHost(Long id, String hostEmail) {
        VisitorImfo visitor = visitorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Visitor not found: " + id));

        if ("CHECKED_IN".equalsIgnoreCase(visitor.getStatus())
         || "CHECKED_OUT".equalsIgnoreCase(visitor.getStatus())) {
            throw new RuntimeException("Already checked in, cannot reject");
        }

        visitor.setStatus("REJECTED");
        return visitorRepository.save(visitor);
    }

    @Override
    public List<VisitorImfo> getTodayVisitorsByDepartment(String department) {
        LocalDate today = LocalDate.now();
        return visitorRepository.findAll().stream()
            .filter(v -> department.equalsIgnoreCase(v.getDepartment())
                      && v.getDueDate() != null
                      && v.getDueDate().toLocalDate().equals(today))
            .collect(Collectors.toList());
    }

    // ── ZXing se QR code generate karo — raw bytes ──
    private byte[] generateQrBytes(String data) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 2);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 250, 250, hints);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    // ── QR data string — security isko scan karega ──
    private String buildQrData(VisitorImfo v) {
        return "VMS:ID:" + v.getId()
             + ":NAME:" + v.getName()
             + ":DEPT:" + v.getDepartment()
             + ":PURPOSE:" + v.getPurpose();
    }

    // ── HTML email with inline CID QR image ──
    // FIX: data:image/png;base64 URIs are blocked by almost all email clients.
    //      Instead we attach the PNG as an inline part with a Content-ID (cid:)
    //      and reference it via <img src="cid:qrcode"> — this works everywhere.
    private void sendQrPassEmail(VisitorImfo v) throws Exception {
        String qrData  = buildQrData(v);
        byte[] qrBytes = generateQrBytes(qrData);      // raw PNG bytes

        String dueDate = v.getDueDate() != null
            ? v.getDueDate().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"))
            : "Not specified";

        // Use cid:qrcode — matches the addInline() call below
        String html = """
            <!DOCTYPE html><html><head><meta charset="UTF-8">
            <style>
              body{font-family:'Segoe UI',Arial,sans-serif;background:#f1f5f9;margin:0;padding:20px}
              .card{background:white;max-width:480px;margin:0 auto;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1)}
              .hdr{background:#143E6B;padding:28px 32px;text-align:center}
              .hdr h1{color:white;margin:0;font-size:22px;font-weight:700}
              .hdr p{color:rgba(255,255,255,.75);margin:6px 0 0;font-size:13px}
              .bdy{padding:28px 32px}
              .badge{display:inline-block;background:#dcfce7;color:#166534;font-size:13px;font-weight:700;padding:6px 16px;border-radius:20px;margin-bottom:20px}
              table{width:100%%;border-collapse:collapse}
              td{padding:8px 0;font-size:14px;border-bottom:1px solid #f8fafc}
              .lbl{color:#94a3b8;font-weight:600;width:40%%}
              .val{color:#1e293b;font-weight:600;text-align:right}
              .qr{text-align:center;padding:20px 0;border-top:1px solid #f1f5f9;border-bottom:1px solid #f1f5f9;margin:20px 0}
              .qr img{border-radius:8px;border:2px solid #e2e8f0;display:block;margin:0 auto}
              .qr-lbl{font-size:12px;color:#94a3b8;margin-top:10px}
              .passid{background:#f8fafc;border:1.5px dashed #e2e8f0;border-radius:10px;padding:14px;text-align:center;margin-top:20px}
              .passid .num{font-size:26px;font-weight:800;color:#143E6B;letter-spacing:2px}
              .note{background:#fffbeb;border:1px solid #fcd34d;border-radius:10px;padding:12px 16px;margin-top:16px;font-size:13px;color:#92400e}
              .ftr{background:#f8fafc;padding:16px 32px;text-align:center;font-size:12px;color:#94a3b8}
            </style></head><body>
            <div class="card">
              <div class="hdr">
                <h1>VMS — Visitor Pass</h1>
                <p>Your visit has been approved</p>
              </div>
              <div class="bdy">
                <div class="badge">APPROVED</div>
                <table>
                  <tr><td class="lbl">Name</td><td class="val">%s</td></tr>
                  <tr><td class="lbl">Department</td><td class="val">%s</td></tr>
                  <tr><td class="lbl">Purpose</td><td class="val">%s</td></tr>
                  <tr><td class="lbl">Scheduled</td><td class="val">%s</td></tr>
                  <tr><td class="lbl">Age</td><td class="val">%s yrs</td></tr>
                </table>
                <div class="qr">
                  <img src="cid:qrcode" width="200" height="200" alt="QR Code"/>
                  <div class="qr-lbl">Show this QR code to security at the entrance</div>
                </div>
                <div class="passid">
                  <div style="font-size:11px;color:#94a3b8;margin-bottom:4px;font-weight:600;text-transform:uppercase">Pass ID</div>
                  <div class="num">AD%s</div>
                </div>
                <div class="note">
                  Please show this QR code to the security guard at the entrance.
                  Your pass ID is <strong>AD%s</strong>.
                </div>
              </div>
              <div class="ftr">This is an auto-generated pass. Do not reply to this email.</div>
            </div>
            </body></html>
            """.formatted(
                v.getName(),
                v.getDepartment(),
                v.getPurpose(),
                dueDate,
                v.getAge(),
                v.getId(),
                v.getId()
            );

        MimeMessage msg = mailSender.createMimeMessage();

        // multipart=true is required for inline attachments
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setTo(v.getEmail());
        helper.setSubject("VMS — Your Visit is Approved! QR Pass Inside");
        helper.setText(html, true);

        // Attach QR PNG as inline resource with Content-ID "qrcode"
        // This is what <img src="cid:qrcode"> references in the HTML above
        helper.addInline("qrcode", new ByteArrayResource(qrBytes), "image/png");

        mailSender.send(msg);
        System.out.println("QR Pass email sent to: " + v.getEmail());
    }
}