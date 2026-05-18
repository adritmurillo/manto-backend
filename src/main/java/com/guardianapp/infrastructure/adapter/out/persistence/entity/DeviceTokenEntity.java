package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity for mobile push tokens.
 */
@Entity
@Table(name = "device_tokens", indexes = {
    @Index(name = "idx_device_token_user", columnList = "user_id"),
    @Index(name = "idx_device_token_token", columnList = "device_token", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceTokenEntity {

    @Id
    @Column(name = "device_token_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @Column(name = "device_token", nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "platform", nullable = false, length = 32)
    private String platform;

    @Column(name = "device_token_created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "device_token_updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (id == null) {
            id = UUID.randomUUID();
        }
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
