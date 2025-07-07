package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.AppSubscription;
import me.femrek.viewcounter.model.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<AppSubscription, UUID> {
    List<AppSubscription> findAllByCreatedBy(GithubUser createdBy);
}
