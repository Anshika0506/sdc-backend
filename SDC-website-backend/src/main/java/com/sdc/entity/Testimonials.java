package com.sdc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="testimonials")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Testimonials {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer testId;
	
	@Column
	private String clientName;

	@Column
	private String des;

	public Testimonials(String clientName, String des) {
		super();
		this.clientName = clientName;
		this.des = des;
	}
	
	
	
}
