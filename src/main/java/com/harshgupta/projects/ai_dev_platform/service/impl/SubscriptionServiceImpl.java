package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutRequest;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.PortalResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.SubscriptionResponse;
import com.harshgupta.projects.ai_dev_platform.enums.SubscriptionStatus;
import com.harshgupta.projects.ai_dev_platform.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getCurrentSubscription() {
        return null;
    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {

    }

    @Override
    public void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }
}
