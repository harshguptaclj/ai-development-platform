package com.harshgupta.projects.ai_dev_platform.mapper;

import com.harshgupta.projects.ai_dev_platform.dto.subscription.PlanResponse;
import com.harshgupta.projects.ai_dev_platform.dto.subscription.SubscriptionResponse;
import com.harshgupta.projects.ai_dev_platform.entity.Plan;
import com.harshgupta.projects.ai_dev_platform.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    PlanResponse toPlanResponse(Plan plan);

}
