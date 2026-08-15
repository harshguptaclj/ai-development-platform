package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

interface PlanRepository extends JpaRepository<Plan, Long> {
}
