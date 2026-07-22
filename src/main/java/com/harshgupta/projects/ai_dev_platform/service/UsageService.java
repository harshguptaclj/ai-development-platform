package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.PlanLimitsResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsageOfUser();

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser();
}
