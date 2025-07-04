package com.sdc.repo;

import com.sdc.entity.Testimonials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestimonialsRepository extends JpaRepository<Testimonials, Long > {
   //all crud database methods
}
