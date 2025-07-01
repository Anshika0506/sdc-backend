package com.sdc.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "contact")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Contact {
	// this is a test
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer contactId;
		
	    @Column
		private String name;
		 
		 @Column 
	    private String email;
		 
		 @Column
	    private String contactNo;
		 
		 @Column
		private String query;
		 
		 @Column
		private String message;
		
	}

