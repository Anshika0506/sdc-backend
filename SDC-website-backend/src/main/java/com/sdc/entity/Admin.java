package com.sdc.entity;

import jakarta.persistence.*;

<<<<<<< HEAD
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String name;
    private String email;
    private String contactNo;
    private String password;

    // Constructors
    public Admin() {}

    public Admin(String name, String email, String contactNo, String password) {
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
        this.password = password;
    }

    // Getters & Setters
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
=======
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Admin {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    
    @Column
    private String name;
    
    @Column(name="email" , nullable = false, unique = true)
    private String email;
    
    @Column
    private String contactNo;
    
    @Column
    private String password;

	public Admin(String name, String email, String contactNo, String password) {
		super();
		this.name = name;
		this.email = email;
		this.contactNo = contactNo;
		this.password = password;
	}
    
    

>>>>>>> 13913549b7d1085aba9eae7f4c2ce8f7600eb511
}