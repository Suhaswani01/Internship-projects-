package com.visitor.flow.Visitor_flow.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visitor.flow.Visitor_flow.entity.VisitorImfo;
import com.visitor.flow.Visitor_flow.repository.VisitorRepository;

@Service
public class VisitorimfoImple implements VisitorImfoService {

    @Autowired
    private VisitorRepository repo;


   
    @Override
    public VisitorImfo addVisitors(VisitorImfo v1) {
        return repo.save(v1);
    }



	@Override
	public List<VisitorImfo> getAllVisitors() {
		
		return repo.findAll();
	}


	   @Override
	    public VisitorImfo approve(Long id) {
	        VisitorImfo v = repo.findById(id).orElseThrow();
	        v.setStatus("APPROVED");
	        return repo.save(v);
	    }

	    
	    @Override
	    public VisitorImfo reject(Long id) {
	        VisitorImfo v = repo.findById(id).orElseThrow();
	        v.setStatus("REJECTED");
	        return repo.save(v);
	    }

	    
	    @Override
	    public VisitorImfo checkIn(Long id) {
	        VisitorImfo v = repo.findById(id).orElseThrow();
	        v.setStatus("CHECKED_IN");
	        v.setCheckInTime(LocalDateTime.now());
	        return repo.save(v);
	    }

	    // Check Out
	    @Override
	    public VisitorImfo checkOut(Long id) {
	        VisitorImfo v = repo.findById(id).orElseThrow();
	        v.setStatus("CHECKED_OUT");
	        v.setCheckOutTime(LocalDateTime.now());
	        return repo.save(v);
	    }
         
	    @Override
	    public List<VisitorImfo> getPendingVisitors() {
	        return repo.findByStatus("PENDING"); 
	    }


	
	}

