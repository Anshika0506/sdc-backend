package com.sdc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sdc.entity.Admin;
import com.sdc.models.AdminModel;
import com.sdc.repo.AdminRepository;

@Service
public class AdminService {
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public Boolean saveAdmin(AdminModel model) {
		// TODO Auto-generated method stub
		
		String rawPassword = model.getPassword();
		String hashedPass = passwordEncoder.encode(rawPassword);
		
		Admin newAdmin = new Admin(model.getName(), model.getEmail(), model.getContact_no(), hashedPass);
		
		adminRepo.save(newAdmin);
		
		System.out.println("admin saved successfully");
		
		return true;
	}
	
	

}
