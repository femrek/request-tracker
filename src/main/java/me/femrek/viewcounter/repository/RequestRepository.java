package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.AppRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<AppRequest, Long> {
}
