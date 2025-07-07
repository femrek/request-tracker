package me.femrek.viewcounter.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseSubscriptionMinimal {
    private Long count;
}
