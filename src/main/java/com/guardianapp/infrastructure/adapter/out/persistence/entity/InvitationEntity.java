package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for invitations.
 */
@Entity
@Table(name = "invitations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvitationEntity {

    @Id
    @Column(name = "invitation_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "host_id", nullable = false)
    private UUID hostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", insertable = false, updatable = false)
    private UserEntity host;

    @Column(name = "invitation_token", nullable = false, unique = true, length = 20)
    private String token;

    @Column(name = "host_name", length = 100)
    private String hostName;

    @Enumerated(EnumType.STRING)
    @Column(name = "invitation_status", nullable = false, length = 20)
    private InvitationStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "invitation_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "accepted_by_user_id")
    private UUID acceptedByUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accepted_by_user_id", insertable = false, updatable = false)
    private UserEntity acceptedByUser;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
