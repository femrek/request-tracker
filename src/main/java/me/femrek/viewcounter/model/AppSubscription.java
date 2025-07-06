package me.femrek.viewcounter.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "subscription")
@Data
public class AppSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(targetEntity = GithubUser.class)
    @JoinColumn(name = "created_by")
    private GithubUser createdBy;

    @Column(name = "counter", nullable = false)
    private Long counter = 0L;

    @OneToMany(targetEntity = AppRequest.class)
    @JoinColumn(name = "subscription")
    private List<AppRequest> requests;
}
