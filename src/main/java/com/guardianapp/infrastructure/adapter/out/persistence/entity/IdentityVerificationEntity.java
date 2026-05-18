package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for identity verifications.
 */
@Entity
@Table(name = "identity_verifications", indexes = {
    @Index(name = "idx_verification_link_id", columnList = "link_id"),
    @Index(name = "idx_verification_host_id", columnList = "host_user_id"),
    @Index(name = "idx_verification_protected_id", columnList = "protected_user_id"),
    @Index(name = "idx_verification_status", columnList = "verification_status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdentityVerificationEntity {

    @Id
    @Column(name = "verification_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "link_id", nullable = false)
    private UUID linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", insertable = false, updatable = false)
    private LinkEntity link;

    @Column(name = "protected_user_id", nullable = false)
    private UUID protectedUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "protected_user_id", insertable = false, updatable = false)
    private UserEntity protectedUser;

    @Column(name = "host_user_id", nullable = false)
    private UUID hostUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", insertable = false, updatable = false)
    private UserEntity hostUser;

    @Column(name = "claimed_person", nullable = false, length = 100)
    private String claimedPerson;

    @Column(name = "challenge_code", nullable = false, length = 20)
    private String challengeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 20)
    private VerificationStatus status;

    @Column(name = "verification_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_note", length = 500)
    private String resolutionNote;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
