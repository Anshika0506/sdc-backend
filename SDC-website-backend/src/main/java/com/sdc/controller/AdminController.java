package com.sdc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdc.models.AdminModel;
import com.sdc.services.AdminService;
import com.sdc.utils.ApiResponse;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // applies to all methods
public class AdminController {
	
	  @Autowired
	    private AdminService adminService;

	  @PostMapping("/saveAdmin")
	    public ResponseEntity<ApiResponse> saveAdmin(@RequestBody AdminModel model)
	    {
	    	Boolean status = adminService.saveAdmin(model);
	    	
	    	if(status==true)
	    	{
	    		return ResponseEntity.ok(new ApiResponse(status, "Admin saved sucessfully", model));
	    	}
	    	
	    	
	    	return ResponseEntity.ok(new ApiResponse(status, "Admin Not Saved", model));
	    	
	    }
	  
	  
	    
	
}
