package hu.szarvas.backend.mapper.request;

import hu.szarvas.backend.model.TierPlan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TierPlanMapper {
    public static TierPlan toTierPlan(String plan) {
        try {
            return TierPlan.valueOf(plan);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown tier plan: {}", plan);
            return TierPlan.TIER_FOUR;
        }
    }
}
