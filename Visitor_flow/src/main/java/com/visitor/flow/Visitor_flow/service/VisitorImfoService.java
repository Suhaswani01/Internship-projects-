package com.visitor.flow.Visitor_flow.service;


import java.util.List;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;

public interface VisitorImfoService {

	
	public VisitorImfo addVisitors(VisitorImfo v1);
	
	public List<VisitorImfo> getAllVisitors() ;

	public List<VisitorImfo> getPendingVisitors(); 

	VisitorImfo approve(Long id);

	VisitorImfo reject(Long id);

	VisitorImfo checkIn(Long id);

	VisitorImfo checkOut(Long id);
	 
}
