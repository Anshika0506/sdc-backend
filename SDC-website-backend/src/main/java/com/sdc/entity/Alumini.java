package com.sdc.entity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "alumini")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Alumini {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private  Integer aluminiId;
	
	@Column
	private String aluminiName;
	
	@Column
	private Integer lpa;
	
	@Column
	private String CompanyName;
	
	@Column
	private String Content;
}
