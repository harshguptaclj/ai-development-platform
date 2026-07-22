package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.PlanLimitsResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.UsageTodayResponse;
import com.harshgupta.projects.ai_dev_platform.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser() {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser() {
        return null;
    }
}
