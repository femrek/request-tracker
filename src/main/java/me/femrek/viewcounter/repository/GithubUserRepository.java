package me.femrek.viewcounter.repository;

import me.femrek.viewcounter.model.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GithubUserRepository extends JpaRepository<GithubUser, Integer> {
}
