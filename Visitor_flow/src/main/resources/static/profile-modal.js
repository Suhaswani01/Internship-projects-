

// ── 1. Inject CSS ──
const _pmStyle = document.createElement('style');
_pmStyle.textContent = `
.pm-overlay {
  display:none; position:fixed; inset:0;
  background:rgba(0,0,0,0.45); z-index:9999;
  align-items:center; justify-content:center;
}
.pm-overlay.open { display:flex; }
.pm-box {
  background:white; border-radius:20px; padding:32px 28px;
  width:100%; max-width:420px;
  animation: pmPop .3s ease;
  box-shadow:0 20px 60px rgba(0,0,0,0.25);
}
@keyframes pmPop {
  from { opacity:0; transform:scale(0.9) translateY(20px); }
  to   { opacity:1; transform:scale(1) translateY(0); }
}
.pm-header {
  display:flex; align-items:center;
  justify-content:space-between; margin-bottom:20px;
}
.pm-header h3 { font-size:17px; font-weight:700; color:#1e293b; }
.pm-close {
  background:#f1f5f9; border:none; border-radius:8px;
  width:32px; height:32px; cursor:pointer; font-size:16px;
  display:flex; align-items:center; justify-content:center;
}
.pm-close:hover { background:#e2e8f0; }
.pm-avatar {
  width:64px; height:64px; border-radius:50%;
  background:#143E6B; color:white; font-size:22px; font-weight:700;
  display:flex; align-items:center; justify-content:center;
  margin:0 auto 6px;
}
.pm-role-badge {
  text-align:center; margin-bottom:18px;
  font-size:12px; font-weight:700; letter-spacing:.05em;
  color:#143E6B; background:#e0e7ff; display:inline-block;
  padding:3px 12px; border-radius:20px;
  width:100%; box-sizing:border-box;
}
.pm-tabs {
  display:flex; gap:6px; margin-bottom:18px;
  background:#f1f5f9; border-radius:10px; padding:4px;
}
.pm-tab {
  flex:1; padding:8px; border:none; background:transparent;
  border-radius:8px; font-size:13px; font-weight:600;
  cursor:pointer; color:#64748b; transition:0.2s;
  font-family:inherit;
}
.pm-tab.active {
  background:white; color:#143E6B;
  box-shadow:0 1px 4px rgba(0,0,0,0.1);
}
.pm-panel { display:none; }
.pm-panel.active { display:block; }
.pm-group { margin-bottom:13px; }
.pm-group label {
  font-size:12px; font-weight:600; color:#475569;
  display:block; margin-bottom:5px;
}
.pm-group input {
  width:100%; padding:11px 13px;
  border:1.5px solid #e2e8f0; border-radius:10px;
  font-size:14px; font-family:inherit;
  outline:none; transition:0.2s; background:#f8fafc;
}
.pm-group input:focus {
  border-color:#143E6B; background:white;
  box-shadow:0 0 0 3px rgba(20,62,107,0.1);
}
.pm-pass-box { position:relative; }
.pm-pass-box input { padding-right:40px; }
.pm-eye {
  position:absolute; right:11px; top:50%;
  transform:translateY(-50%); cursor:pointer; font-size:15px;
}
.pm-save {
  width:100%; padding:12px; background:#143E6B; color:white;
  border:none; border-radius:10px; font-size:14px; font-weight:700;
  cursor:pointer; font-family:inherit; transition:0.2s; margin-top:4px;
}
.pm-save:hover { background:#0f2d52; }
.pm-save:disabled { background:#94a3b8; cursor:not-allowed; }
.pm-alert {
  padding:9px 13px; border-radius:8px; font-size:13px;
  font-weight:600; margin-bottom:12px; display:none; text-align:center;
}
.pm-alert.show { display:block; }
.pm-alert.err { background:#fee2e2; color:#dc2626; }
.pm-alert.ok  { background:#dcfce7; color:#16a34a; }
`;
document.head.appendChild(_pmStyle);

// ── 2. Inject HTML ──
const _pmHTML = `
<div class="pm-overlay" id="pmOverlay">
  <div class="pm-box">
    <div class="pm-header">
      <h3>My Profile</h3>
      <button class="pm-close" onclick="pmClose()">✕</button>
    </div>
    <div class="pm-avatar" id="pmAvatar">--</div>
    <div style="text-align:center">
      <span class="pm-role-badge" id="pmRoleBadge">--</span>
    </div>
    <div class="pm-tabs">
      <button class="pm-tab active" onclick="pmTab('pm-profile',this)">Profile</button>
      <button class="pm-tab"        onclick="pmTab('pm-password',this)">Password</button>
    </div>

    <!-- Profile Tab -->
    <div class="pm-panel active" id="pm-profile">
      <div class="pm-alert" id="pm-profile-alert"></div>
      <div class="pm-group">
        <label>Full Name</label>
        <input type="text" id="pm-name" placeholder="Enter your name">
      </div>
      <div class="pm-group">
        <label>Email Address</label>
        <input type="email" id="pm-email" placeholder="Enter your email">
      </div>
      <button class="pm-save" id="pm-profile-btn" onclick="pmSaveProfile()">
        Save Changes
      </button>
    </div>

    <!-- Password Tab -->
    <div class="pm-panel" id="pm-password">
      <div class="pm-alert" id="pm-pass-alert"></div>
      <div class="pm-group">
        <label>Current Password</label>
        <div class="pm-pass-box">
          <input type="password" id="pm-old-pass" placeholder="Enter current password">
          <span class="pm-eye" onclick="pmToggleEye('pm-old-pass',this)">👁</span>
        </div>
      </div>
      <div class="pm-group">
        <label>New Password</label>
        <div class="pm-pass-box">
          <input type="password" id="pm-new-pass" placeholder="Min 6 characters">
          <span class="pm-eye" onclick="pmToggleEye('pm-new-pass',this)">👁</span>
        </div>
      </div>
      <div class="pm-group">
        <label>Confirm New Password</label>
        <div class="pm-pass-box">
          <input type="password" id="pm-confirm-pass" placeholder="Re-enter new password">
          <span class="pm-eye" onclick="pmToggleEye('pm-confirm-pass',this)">👁</span>
        </div>
      </div>
      <button class="pm-save" id="pm-pass-btn" onclick="pmSavePassword()">
        Update Password
      </button>
    </div>

  </div>
</div>`;
document.body.insertAdjacentHTML('beforeend', _pmHTML);

// ── 3. Open / Close ──
function pmOpen() {
  const name  = sessionStorage.getItem('name')  || 'User';
  const role  = sessionStorage.getItem('role')  || '--';
  const email = sessionStorage.getItem('email') || '';

  document.getElementById('pm-name').value  = name;
  document.getElementById('pm-email').value = email;
  document.getElementById('pmRoleBadge').textContent = role;

  const initials = name.trim().split(' ')
    .map(n => n[0]).join('').toUpperCase().slice(0,2);
  document.getElementById('pmAvatar').textContent = initials;
  document.getElementById('pmOverlay').classList.add('open');
}
function pmClose() {
  document.getElementById('pmOverlay').classList.remove('open');
}
document.getElementById('pmOverlay').addEventListener('click', function(e) {
  if (e.target === this) pmClose();
});

// ── 4. Tab switch ──
function pmTab(id, btn) {
  document.querySelectorAll('.pm-panel').forEach(p => p.classList.remove('active'));
  document.querySelectorAll('.pm-tab').forEach(b => b.classList.remove('active'));
  document.getElementById(id).classList.add('active');
  btn.classList.add('active');
}

// ── 5. Toggle eye ──
function pmToggleEye(id, el) {
  const inp = document.getElementById(id);
  inp.type = inp.type === 'password' ? 'text' : 'password';
  el.textContent = inp.type === 'password' ? '👁' : '🙈';
}

// ── 6. Alert helper ──
function pmAlert(id, msg, type) {
  const el = document.getElementById(id);
  el.textContent = msg;
  el.className = `pm-alert ${type} show`;
  setTimeout(() => el.classList.remove('show'), 4000);
}

// ── 7. Save Profile ──
async function pmSaveProfile() {
  const name         = document.getElementById('pm-name').value.trim();
  const newEmail     = document.getElementById('pm-email').value.trim();
  const currentEmail = sessionStorage.getItem('email');

  if (!name || !newEmail) {
    pmAlert('pm-profile-alert','⚠️ Please fill all fields!','err'); return;
  }

  const btn = document.getElementById('pm-profile-btn');
  btn.disabled = true; btn.textContent = 'Saving…';

  try {
    const res = await fetch('/auth/update-profile', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ currentEmail, name, newEmail })
    });
    const data = await res.json();
    if (!res.ok) { pmAlert('pm-profile-alert','❌ '+data.message,'err'); return; }

    sessionStorage.setItem('name',  name);
    sessionStorage.setItem('email', newEmail);

    // Sidebar live update (agar elements exist karein)
    const uName   = document.getElementById('userName');
    const uAvatar = document.getElementById('userAvatar');
    const initials = name.trim().split(' ').map(n=>n[0]).join('').toUpperCase().slice(0,2);
    if (uName)   uName.textContent   = name;
    if (uAvatar) uAvatar.textContent = initials;
    document.getElementById('pmAvatar').textContent = initials;

    pmAlert('pm-profile-alert','✅ Profile updated successfully!','ok');
  } catch {
    pmAlert('pm-profile-alert','⚠️ Server not reachable!','err');
  } finally {
    btn.disabled = false; btn.textContent = 'Save Changes';
  }
}

// ── 8. Save Password ──
async function pmSavePassword() {
  const email    = sessionStorage.getItem('email');
  const oldPass  = document.getElementById('pm-old-pass').value.trim();
  const newPass  = document.getElementById('pm-new-pass').value.trim();
  const confirm  = document.getElementById('pm-confirm-pass').value.trim();

  if (!oldPass || !newPass || !confirm) {
    pmAlert('pm-pass-alert','⚠️ Please fill all fields!','err'); return;
  }
  if (newPass.length < 6) {
    pmAlert('pm-pass-alert','⚠️ Min 6 characters required!','err'); return;
  }
  if (newPass !== confirm) {
    pmAlert('pm-pass-alert','❌ Passwords do not match!','err'); return;
  }

  const btn = document.getElementById('pm-pass-btn');
  btn.disabled = true; btn.textContent = 'Updating…';

  try {
    const res = await fetch('/auth/change-password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, oldPassword: oldPass, newPassword: newPass })
    });
    const data = await res.json();
    if (!res.ok) { pmAlert('pm-pass-alert','❌ '+data.message,'err'); return; }

    pmAlert('pm-pass-alert','✅ Password updated!','ok');
    document.getElementById('pm-old-pass').value    = '';
    document.getElementById('pm-new-pass').value    = '';
    document.getElementById('pm-confirm-pass').value = '';
  } catch {
    pmAlert('pm-pass-alert','⚠️ Server not reachable!','err');
  } finally {
    btn.disabled = false; btn.textContent = 'Update Password';
  }
}

// ── 9. Auto-setup sidebar user info ──
document.addEventListener('DOMContentLoaded', () => {
  const name  = sessionStorage.getItem('name')  || 'User';
  const role  = sessionStorage.getItem('role')  || '--';
  const email = sessionStorage.getItem('email') || '';

  const initials = name.trim().split(' ')
    .map(n => n[0]).join('').toUpperCase().slice(0,2);

  // Sab pages ke common IDs update karo
  const ids = {
    'userName':   name,
    'userRole':   role,
    'userAvatar': initials,
    'userEmail':  email,
  };
  Object.entries(ids).forEach(([id, val]) => {
    const el = document.getElementById(id);
    if (el) el.textContent = val;
  });

  // profileBtn click — sab pages pe
  const btn = document.getElementById('profileBtn');
  if (btn) btn.addEventListener('click', pmOpen);
});