package com.visitor.flow.Visitor_flow.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

	
	 
	@Controller
	public class IndexController {
	 
	    // Root URL pe aao toh login.html pe bhejo
	    @GetMapping("/")
	    public String index() {
	        return "redirect:/login.html";
	    }
	}
	 

