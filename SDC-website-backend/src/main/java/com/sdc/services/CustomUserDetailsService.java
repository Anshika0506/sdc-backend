package com.sdc.services;

import com.sdc.entity.Admin;
import com.sdc.repo.AdminRepository;
import com.sdc.utils.AdminUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        System.out.println("=================================");
        System.out.println("Searching Email = [" + email + "]");

        System.out.println("All Admins In Database:");

        adminRepository.findAll().forEach(admin -> {
            System.out.println(
                    "ID=" + admin.getAdminId()
                            + " EMAIL=[" + admin.getEmail() + "]"
            );
        });

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("ADMIN NOT FOUND");
                    return new UsernameNotFoundException("Admin not found");
                });

        System.out.println("ADMIN FOUND = " + admin.getEmail());

        return new AdminUserDetails(admin);
    }
}