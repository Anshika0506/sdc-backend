package com.sdc.models;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestimonialsModel {
	
	private String clientName;
	
	private String des;
	
	private MultipartFile image;

}
