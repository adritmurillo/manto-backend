package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.LinkStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for persisting links in the database.
 * This class is separated from the domain model to maintain independence.
 */
@Entity
@Table(name = "links", 
       uniqueConstraints = {
           @UniqueConstraint(
               name = "uk_link_host_protected_active",
               columnNames = {"host_id", "protected_id", "link_status"}
           )
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkEntity {

    @Id
    @Column(name = "link_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @Column(name = "protected_id", nullable = false)
    private UUID protectedId;

    @Column(name = "connection_code", nullable = false, length = 6)
    private String connectionCode;

    @Column(name = "code_created_at", nullable = false)
    private LocalDateTime codeCreatedAt;

    @Column(name = "code_expires_at", nullable = false)
    private LocalDateTime codeExpiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "link_status", nullable = false, length = 20)
    private LinkStatus status;

    @Column(name = "link_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "link_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
