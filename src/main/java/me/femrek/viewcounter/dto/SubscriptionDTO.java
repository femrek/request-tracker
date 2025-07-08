package me.femrek.viewcounter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.femrek.viewcounter.model.AppSubscription;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SubscriptionDTO {
    private String id;
    private String name;
    private Long count;
    private String restEndpoint;
    private String svgEndpoint;
    private Timestamp createdAt;

    public SubscriptionDTO(AppSubscription subscription) {
        this.id = subscription.getId().toString();
        this.name = subscription.getName();
        this.count = subscription.getCounter();
        this.restEndpoint = "/api/subscriptions/request/" + subscription.getId();
        this.svgEndpoint = "/api/subscriptions/request/badge/" + subscription.getId();
        this.createdAt = subscription.getCreatedAt();
    }

    public static List<SubscriptionDTO> from(List<AppSubscription> subscriptions) {
        return subscriptions.stream()
                .map(SubscriptionDTO::new)
                .toList();
    }
}
