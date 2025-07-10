package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.AppRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<AppRequest, Long> {
    List<AppRequest> findTop10BySubscription_IdOrderByTimestampDesc(UUID subscriptionId);
}
