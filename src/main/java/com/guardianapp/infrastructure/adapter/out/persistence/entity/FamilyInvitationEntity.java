package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.FamilyInvitationStatus;
import com.guardianapp.domain.enums.FamilyMemberRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for family invitations.
 */
@Entity
@Table(name = "family_invitations", indexes = {
        @Index(name = "idx_family_invitation_group", columnList = "family_group_id"),
        @Index(name = "idx_family_invitation_inviter", columnList = "invited_by_user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyInvitationEntity {

    @Id
    @Column(name = "family_invitation_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "family_group_id", nullable = false)
    private UUID familyGroupId;

    @ManyToOne
    @JoinColumn(name = "family_group_id", insertable = false, updatable = false)
    private FamilyGroupEntity familyGroup;

    @Column(name = "invited_by_user_id", nullable = false)
    private UUID invitedByUserId;

    @ManyToOne
    @JoinColumn(name = "invited_by_user_id", insertable = false, updatable = false)
    private UserEntity invitedByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role", nullable = false, length = 30)
    private FamilyMemberRole targetRole;

    @Column(name = "family_invitation_token", nullable = false, unique = true, length = 20)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "family_invitation_status", nullable = false, length = 20)
    private FamilyInvitationStatus status;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "family_invitation_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "accepted_by_user_id")
    private UUID acceptedByUserId;

    @ManyToOne
    @JoinColumn(name = "accepted_by_user_id", insertable = false, updatable = false)
    private UserEntity acceptedByUser;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
