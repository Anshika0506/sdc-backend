package com.sdc.repo;

import com.sdc.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeammemberRepo extends JpaRepository<TeamMember, Integer> {
}

