package me.femrek.viewcounter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class BadgeService {
    @Value(value = "${shieldUrl}")
    private String shieldUrl;

    private final RestClient restClient;
    private final SubscriptionRepository subscriptionRepository;

    public String getViewsBadge(String uuid, Map<String, String> queryParams) {
        AppSubscription subscription = subscriptionRepository.findById(UUID.fromString(uuid)).orElseThrow(() ->
                new IllegalArgumentException("Subscription not found for UUID: " + uuid));
        return getViewsBadge(subscription, queryParams);
    }

    public String getViewsBadge(AppSubscription subscription, Map<String, String> queryParams) {
        if (subscription == null) {
            log.error("Subscription is null. Cannot generate badge.");
            throw new IllegalArgumentException("Subscription cannot be null.");
        }

        Long views = subscription.getCounter();

        String label = "views";
        if (queryParams.containsKey("label")) {
            label = queryParams.remove("label");
        }

        String color = "blue";
        if (queryParams.containsKey("color")) {
            color = queryParams.remove("color");
        }

        return _getViewsBadge(views, label, color, queryParams);
    }

    private String _getViewsBadge(Long views, String label, String color, Map<String, String> query) {
        log.trace("Generating badge with label: {}, message: {}, color: {}, query: {}",
                label, views, color, query);
        String message = views == null ? "0" : String.valueOf(views);
        String queue = String.format("/badge/%s-%s-%s.svg", label, message, color);
        if (shieldUrl == null || shieldUrl.isEmpty()) {
            log.error("Shield URL is not configured. Please set 'shieldUrl' in application properties.");
            throw new IllegalStateException("Shield URL is not configured.");
        }
        if (shieldUrl.endsWith("/")) {
            shieldUrl = shieldUrl.substring(0, shieldUrl.length() - 1);
        }

        URI uri = URI.create(shieldUrl + queue);
        String response = restClient.get()
                .uri(UriComponentsBuilder
                        .fromUri(uri)
                        .queryParams(MultiValueMap.fromSingleValue(query))
                        .build().toUri())
                .retrieve()
                .body(String.class);
        log.trace("Generated badge with label: {}, message: {}, color: {}, query: {} | response: {}",
                label, message, color, query, response);

        return response;
    }
}
