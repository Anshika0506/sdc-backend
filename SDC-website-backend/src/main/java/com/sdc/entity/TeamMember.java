package com.sdc.entity;

import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="teamMember")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TeamMember {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;
	
	@Column
	private String name;
	
	@Column
	private String branch;
	
	@Column
	private String position;
	
	@Column
	private String linkdin_url;
	
	@Column
	private String github_url;

	@Column
	private String insta_url;
	
    // Image stored as byte array in DB
	@Lob
	@Column(name = "image", columnDefinition = "LONGBLOB")
	private byte[] image;
	
	
	@ManyToMany(mappedBy = "teamMembers")
	@JsonBackReference
	@JsonIgnore
	private List<Projects> projects = new ArrayList<>() ;
	
}
