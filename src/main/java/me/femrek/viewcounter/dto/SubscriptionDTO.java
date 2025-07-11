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
    private Timestamp lastRequestAt;
    private List<RequestDTO> requests;

    public SubscriptionDTO(AppSubscription subscription) {
        this.id = subscription.getId().toString();
        this.name = subscription.getName();
        this.count = subscription.getCounter();
        this.restEndpoint = "/api/subscriptions/request/" + subscription.getId();
        this.svgEndpoint = "/api/subscriptions/request/badge/" + subscription.getId();
        this.createdAt = subscription.getCreatedAt();
        this.lastRequestAt = subscription.getLastRequestAt();
        this.requests = subscription.getRequests().stream()
                .map(RequestDTO::new)
                .toList();
    }

    public static List<SubscriptionDTO> from(List<AppSubscription> subscriptions) {
        return subscriptions.stream()
                .map(SubscriptionDTO::new)
                .toList();
    }

    public String lastRequestAtFormatted() {
        if (lastRequestAt == null) {
            return null;
        }
        final Timestamp now = new Timestamp(System.currentTimeMillis());

        if (lastRequestAt.toLocalDateTime().getYear() == now.toLocalDateTime().getYear()
                && lastRequestAt.toLocalDateTime().getDayOfYear() == now.toLocalDateTime().getDayOfYear()) {
            return String.format("Today %02d:%02d:%02d",
                    lastRequestAt.toLocalDateTime().getHour(),
                    lastRequestAt.toLocalDateTime().getMinute(),
                    lastRequestAt.toLocalDateTime().getSecond());
        } else {
            return String.format("%s %02d:%02d:%02d",
                    lastRequestAt.toLocalDateTime().toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    lastRequestAt.toLocalDateTime().getHour(),
                    lastRequestAt.toLocalDateTime().getMinute(),
                    lastRequestAt.toLocalDateTime().getSecond());
        }
    }
}
