package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.model.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<AppSubscription, UUID> {
    List<AppSubscription> findAllByCreatedByAndIsDeletedFalseOrderByCreatedAtAsc(GithubUser createdBy);
    List<AppSubscription> findAllByCreatedByAndIsDeletedTrueOrderByCreatedAtAsc(GithubUser createdBy);

    Optional<AppSubscription> findByIdAndIsDeletedFalse(UUID id);
    Optional<AppSubscription> findByIdAndIsDeletedTrue(UUID id);
}
