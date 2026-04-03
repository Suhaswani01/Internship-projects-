package com.visitor.flow.Visitor_flow.service;
 
import java.util.List;
import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
 
public interface HostService {
    List<VisitorImfo> getVisitorsByDepartment(String department);
    List<VisitorImfo> getPendingByDepartment(String department);
    VisitorImfo approveByHost(Long id, String hostEmail);
    VisitorImfo rejectByHost(Long id, String hostEmail);
    List<VisitorImfo> getTodayVisitorsByDepartment(String department);
}