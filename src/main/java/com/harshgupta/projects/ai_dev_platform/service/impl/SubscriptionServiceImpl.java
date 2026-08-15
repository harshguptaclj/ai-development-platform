package com.harshgupta.projects.ai_dev_platform.service.impl;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutRequest;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.PortalResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.SubscriptionResponse;
import com.harshgupta.projects.ai_dev_platform.entity.Plan;
import com.harshgupta.projects.ai_dev_platform.entity.Subscription;
import com.harshgupta.projects.ai_dev_platform.entity.User;
import com.harshgupta.projects.ai_dev_platform.enums.SubscriptionStatus;
import com.harshgupta.projects.ai_dev_platform.error.ResourceNotFoundException;
import com.harshgupta.projects.ai_dev_platform.mapper.SubscriptionMapper;
import com.harshgupta.projects.ai_dev_platform.repository.PlanRepository;
import com.harshgupta.projects.ai_dev_platform.repository.SubscriptionRepository;
import com.harshgupta.projects.ai_dev_platform.repository.UserRepository;
import com.harshgupta.projects.ai_dev_platform.security.AuthUtil;
import com.harshgupta.projects.ai_dev_platform.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final AuthUtil authUtil;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Override
    public SubscriptionResponse getCurrentSubscription() {
        Long userId = authUtil.getCurrentUserId();

        var currentSubscription =  subscriptionRepository.findByUserIdAndStatusIn(userId, Set.of(
                SubscriptionStatus.ACTIVE, SubscriptionStatus.PAST_DUE, SubscriptionStatus.TRIALING
        )).orElse(
                new Subscription()
        );
        return subscriptionMapper.toSubscriptionResponse(currentSubscription);

    }

    @Override
    public void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId) {
        boolean exists = subscriptionRepository.existsByStripeSubscriptionId(subscriptionId);
        if (exists) return;

        User user = getCurrentUser(userId);
        Plan plan = getCurrentPlan(planId);

        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .stripeSubscriptionId(subscriptionId)
                .status(SubscriptionStatus.INCOMPLETE)
                .build();

        subscriptionRepository.save(subscription);


    }

    @Override
    public void updateSubscription(String subId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId) {

    }

    @Override
    public void cancelSubscription(String subId) {

    }

    @Override
    public void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd) {

        Subscription subscription = getCurrentSubscription(gatewaySubscriptionId);

        Instant newStart = periodStart != null ? periodStart : subscription.getCurrentPeriodEnd();
        subscription.setCurrentPeriodStart(newStart);
        subscription.setCurrentPeriodEnd(periodEnd);

        if(subscription.getStatus() == SubscriptionStatus.PAST_DUE){
            subscription.setStatus(SubscriptionStatus.ACTIVE);
        }

    }

    @Override
    public void markSubscriptionPastDue(String subId) {

    }

    private User getCurrentUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", userId.toString())
        );
    }

    private Plan getCurrentPlan(Long planId) {
        return planRepository.findById(planId).orElseThrow(
                () -> new ResourceNotFoundException("Plan", planId.toString())
        );
    }

    private Subscription getCurrentSubscription(String gatewaySubscriptionId) {
        return subscriptionRepository.findByStripeSubscriptionId(gatewaySubscriptionId).orElseThrow(
                ()-> new ResourceNotFoundException("Subscription", gatewaySubscriptionId)
        );
    }
}
