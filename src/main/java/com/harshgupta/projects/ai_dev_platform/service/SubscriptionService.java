package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutRequest;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.PortalResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.SubscriptionResponse;
import com.harshgupta.projects.ai_dev_platform.enums.SubscriptionStatus;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String id, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);
}
