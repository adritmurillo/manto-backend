package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * JPA entity for family groups.
 */
@Entity
@Table(name = "family_groups", indexes = {
        @Index(name = "idx_family_group_primary_host", columnList = "primary_host_user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupEntity {

    @Id
    @Column(name = "family_group_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "family_group_name", nullable = false, length = 100)
    private String name;

    @Column(name = "primary_host_user_id", nullable = false)
    private UUID primaryHostUserId;

    @ManyToOne
    @JoinColumn(name = "primary_host_user_id", insertable = false, updatable = false)
    private UserEntity primaryHostUser;

    @Column(name = "family_group_created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
