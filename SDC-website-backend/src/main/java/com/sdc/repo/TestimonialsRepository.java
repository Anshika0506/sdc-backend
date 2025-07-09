package com.sdc.repo;

import com.sdc.entity.Testimonials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialsRepository extends JpaRepository<Testimonials, Integer> {
    
}
