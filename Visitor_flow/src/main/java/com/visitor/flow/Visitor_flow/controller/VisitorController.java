package com.visitor.flow.Visitor_flow.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.service.VisitorImfoService;

@CrossOrigin(origins = "*")
@RestController
public class VisitorController {
	@Autowired
	private VisitorImfoService visitorservice;
	
	 @GetMapping("/home")
	public String home(){
		
		return "returmb njxhgjda";
	}
	 

	
	 @PostMapping("/add")
	 public VisitorImfo addVisitors(@RequestBody VisitorImfo v1) {
		 
		 return this.visitorservice.addVisitors(v1);
	 }
	 
	 @GetMapping("/visitors")
	 public List<VisitorImfo> getAllVisitors() {
	     return visitorservice.getAllVisitors();
	 }
	 
	 @PutMapping("/{id}/approve")
	    public VisitorImfo approve(@PathVariable Long id) {
	       
	      
	        return visitorservice.approve(id);
	    }

	    // Reject
	    @PutMapping("/{id}/reject")
	    public VisitorImfo reject(@PathVariable Long id) {
	        
	      
	        return visitorservice.reject(id);
	    }

	    // Check In
	    @PutMapping("/{id}/checkin")
	    public VisitorImfo checkIn(@PathVariable Long id) {
	     
	       
	        return visitorservice.checkIn(id);
	    }
 
	    @PutMapping("/{id}/checkout")
	    public VisitorImfo checkOut(@PathVariable Long id) {
	    
	        return visitorservice.checkOut(id);
	    }
	    
	    @GetMapping("/visitors/pending")
	    public List<VisitorImfo> getPendingVisitors() {
	        return visitorservice.getPendingVisitors(); // ✅
	    }
	    
}
