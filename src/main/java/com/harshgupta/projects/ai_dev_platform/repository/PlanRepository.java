package com.harshgupta.projects.ai_dev_platform.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.harshgupta.projects.ai_dev_platform.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Optional<Plan> findByStripePriceId(String id);
}
