package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.AppSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<AppSubscription, UUID> {
}
