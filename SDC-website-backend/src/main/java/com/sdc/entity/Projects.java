package com.sdc.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="projects")
@AllArgsConstructor
@NoArgsConstructor
@Data


public class Projects {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectID;
	
	@Column
	private String title;

	@Column(name="imageURL")
	private String thumbnail;
	
	@Column
	private String discription;
	
	@Column
	private String link;
	
	@ManyToMany
	@JoinTable(
	        name = "project_team_member",
	        joinColumns = @JoinColumn(name = "projectID"),
	        inverseJoinColumns = @JoinColumn(name = "memberId")
	    )
	private List<TeamMember> teamMembers = new ArrayList<>();
	
	
}
