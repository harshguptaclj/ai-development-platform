package com.harshgupta.projects.ai_dev_platform.service;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutRequest;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.CheckoutResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
