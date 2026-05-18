package com.guardianapp.infrastructure.adapter.out.persistence.entity;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for family group members.
 */
@Entity
@Table(
        name = "family_group_members",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_family_group_user", columnNames = {"family_group_id", "user_id"})
        },
        indexes = {
                @Index(name = "idx_family_member_group", columnList = "family_group_id"),
                @Index(name = "idx_family_member_user", columnList = "user_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupMemberEntity {

    @Id
    @Column(name = "family_group_member_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "family_group_id", nullable = false)
    private UUID familyGroupId;

    @ManyToOne
    @JoinColumn(name = "family_group_id", insertable = false, updatable = false)
    private FamilyGroupEntity familyGroup;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "family_group_member_role", nullable = false, length = 30)
    private FamilyMemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (joinedAt == null) {
            joinedAt = LocalDateTime.now();
        }
    }
}
