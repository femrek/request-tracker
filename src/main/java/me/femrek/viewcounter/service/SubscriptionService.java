package me.femrek.viewcounter.service;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.femrek.viewcounter.dto.RequestRequest;
import me.femrek.viewcounter.dto.SubscriptionDTO;
import me.femrek.viewcounter.dto.UpdateSubscription;
import me.femrek.viewcounter.error.AppSubscriptionNotFound;
import me.femrek.viewcounter.model.AppRequest;
import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.model.GithubUser;
import me.femrek.viewcounter.repository.RequestRepository;
import me.femrek.viewcounter.repository.SubscriptionRepository;
import me.femrek.viewcounter.security.CustomOAuth2User;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final RequestRepository requestRepository;
    private final EntityManager entityManager;
    private final BadgeService badgeService;

    @Transactional
    public SubscriptionDTO performRequest(UUID subscriptionId, RequestRequest requestRequest) {
        return new SubscriptionDTO(_performRequest(subscriptionId, requestRequest));
    }

    @Transactional
    public String performRequestWithBadge(UUID subscriptionId,
                                          RequestRequest requestRequest,
                                          Map<String, String> queryParams) {
        AppSubscription subscription = _performRequest(subscriptionId, requestRequest);
        return badgeService.getViewsBadge(subscription, queryParams);
    }

    private AppSubscription _performRequest(UUID subscriptionId, RequestRequest requestRequest) {
        log.trace("Performing request for subscription: {}", requestRequest);
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null.");
        }

        AppSubscription subscription = subscriptionRepository.findByIdAndIsDeletedFalse(subscriptionId)
                .orElseThrow(() -> new AppSubscriptionNotFound(
                        "Subscription with ID " + subscriptionId + " not found."));

        AppRequest request = AppRequest.builder()
                .subscription(subscription)
                .userAgent(requestRequest.getUserAgent())
                .ipAddress(requestRequest.getIpAddress())
                .build();
        requestRepository.saveAndFlush(request);
        entityManager.refresh(subscription);

        log.trace("count {}. Request performed for subscription: {}",
                subscription.getCounter(), subscriptionId);

        return subscription;
    }

    public SubscriptionDTO getSubscription(UUID uuid) {
        log.trace("Fetching subscription with ID: {}", uuid);
        AppSubscription subscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppSubscriptionNotFound("Subscription with ID " + uuid + " not found."));
        return new SubscriptionDTO(subscription);
    }

    @Transactional
    public void createSubscription(CustomOAuth2User user) {
        log.trace("Creating a new subscription");

        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to create a subscription.");
        }
        GithubUser githubUser = user.getGithubUser();
        if (githubUser == null) {
            throw new IllegalArgumentException("User not found: " + user.getName());
        }

        AppSubscription newSubscription = AppSubscription.builder()
                .name("-")
                .counter(0L)
                .createdBy(githubUser)
                .isDeleted(false)
                .build();
        AppSubscription savedSubscription = subscriptionRepository.save(newSubscription);

        log.debug("Created new subscription with ID: {}", savedSubscription.getId());
    }

    @Transactional
    public void updateSubscription(UUID uuid, UpdateSubscription updateSubscription) {
        log.trace("Updating subscription with ID: {}", uuid);

        AppSubscription subscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppSubscriptionNotFound("Subscription with ID " + uuid + " not found."));

        if (updateSubscription.validate() != null) {
            throw new IllegalArgumentException("Invalid update data: " + updateSubscription.validate());
        }

        subscription.setName(updateSubscription.getName());
        AppSubscription updatedSubscription = subscriptionRepository.save(subscription);

        log.debug("Updated subscription with ID: {}", updatedSubscription.getId());
    }

    public void mountSubscriptionsOnlyExist(GithubUser user, boolean limitRequests) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to mount subscriptions.");
        }
        log.trace("Mounting subscriptions for user: {}", user.getUsername());

        List<AppSubscription> subscriptions = subscriptionRepository
                .findAllByCreatedByAndIsDeletedFalseOrderByCreatedAtAsc(user);
        subscriptions.forEach(subscription -> {
            if (limitRequests) subscription.setRequests(requestRepository
                    .findTop10BySubscription_IdOrderByTimestampDesc(subscription.getId()));
        });
        user.setSubscriptions(subscriptions);
    }

    public List<SubscriptionDTO> getSubscriptionsOnlyDeleted(GithubUser user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be authenticated to get deleted subscriptions.");
        }
        log.trace("Fetching deleted subscriptions for user: {}", user.getUsername());
        return SubscriptionDTO.from(subscriptionRepository
                .findAllByCreatedByAndIsDeletedTrueOrderByCreatedAtAsc(user));
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0];
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    @Transactional
    public void deleteSubscription(GithubUser user, UUID uuid) {
        log.trace("Deleting subscription with ID: {}", uuid);
        subscriptionRepository.findByIdAndIsDeletedTrue(uuid).ifPresent(
                subscription -> {
                    log.debug("Subscription with ID {} is already deleted.", uuid);
                    throw new AppSubscriptionNotFound("Subscription with ID " + uuid + " is already deleted.");
                });
        AppSubscription subscription = subscriptionRepository.findByIdAndIsDeletedFalse(uuid)
                .orElseThrow(() -> new AppSubscriptionNotFound("Subscription with ID " + uuid + " not found."));

        if (subscription.getCreatedBy() == null ||
                !subscription.getCreatedBy().getId().equals(user.getId())) {
            log.warn("User {} is not authorized to delete subscription with ID: {}", user.getUsername(), uuid);
            throw new AuthorizationDeniedException("You are not authorized to delete this subscription.");
        }

        subscription.setIsDeleted(true);
        subscriptionRepository.save(subscription);

        log.debug("Deleted subscription with ID: {}", uuid);
    }

    @Transactional
    public void restoreById(GithubUser user, UUID id) {
        log.trace("Restoring subscription with ID: {}", id);
        subscriptionRepository.findByIdAndIsDeletedFalse(id).ifPresent(
                subscription -> {
                    log.debug("Subscription with ID {} is not deleted.", id);
                    throw new AppSubscriptionNotFound("Subscription with ID " + id + " is not deleted.");
                });
        AppSubscription subscription = subscriptionRepository.findByIdAndIsDeletedTrue(id)
                .orElseThrow(() -> new AppSubscriptionNotFound("Subscription with ID " + id + " not found."));

        if (subscription.getCreatedBy() == null ||
                !subscription.getCreatedBy().getId().equals(user.getId())) {
            log.warn("User {} is not authorized to restore subscription with ID: {}", user.getUsername(), id);
            throw new AuthorizationDeniedException("You are not authorized to restore this subscription.");
        }

        subscription.setIsDeleted(false);
        subscriptionRepository.save(subscription);

        log.debug("Restored subscription with ID: {}", id);
    }
}
