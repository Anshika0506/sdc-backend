package com.sdc.repo;

import com.sdc.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepo extends JpaRepository<Projects, Integer> {
}
