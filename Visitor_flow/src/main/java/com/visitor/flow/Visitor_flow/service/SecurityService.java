package com.visitor.flow.Visitor_flow.service;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;

public  interface SecurityService {
   
	  VisitorImfo checkIn(Long id);
	  VisitorImfo checkOut(Long id);
	  VisitorImfo getVisitorById(Long id);
}
