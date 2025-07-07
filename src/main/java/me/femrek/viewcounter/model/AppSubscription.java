package me.femrek.viewcounter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "subscription")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(targetEntity = GithubUser.class)
    @JoinColumn(name = "created_by")
    private GithubUser createdBy;

    @Column(name = "counter", nullable = false)
    private Long counter = 0L;

    @OneToMany(targetEntity = AppRequest.class)
    @JoinColumn(name = "subscription")
    private List<AppRequest> requests;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "last_request_at")
    private Timestamp lastRequestAt;
}
