package me.femrek.viewcounter.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "github_user")
@Data
public class GithubUser {
    @Id
    @Column(name = "gh_id")
    private Integer id;

    @Column(name = "gh_username", nullable = false, unique = true)
    private String username;

    @Column(name = "gh_name")
    private String name;

    @Column(name = "gh_profile_url")
    private String profileUrl;

    @Column(name = "gh_avatar_url")
    private String avatarUrl;

    @OneToMany(targetEntity = AppSubscription.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private List<AppSubscription> subscriptions;
}
