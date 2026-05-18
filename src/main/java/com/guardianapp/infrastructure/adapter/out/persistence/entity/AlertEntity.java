package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.AlertStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for alerts.
 */
@Entity
@Table(name = "alerts", indexes = {
    @Index(name = "idx_alert_link_id", columnList = "link_id"),
    @Index(name = "idx_alert_status", columnList = "alert_status"),
    @Index(name = "idx_alert_protected_user", columnList = "protected_user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertEntity {

    @Id
    @Column(name = "alert_id", nullable = false, updatable = false)
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

    @Column(name = "suspicious_url", nullable = false, length = 2048)
    private String suspiciousUrl;

    @Column(name = "reason", length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_status", nullable = false, length = 20)
    private AlertStatus status;

    @Column(name = "alert_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by_user_id")
    private UUID resolvedByUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_user_id", insertable = false, updatable = false)
    private UserEntity resolvedByUser;

    @Column(name = "resolution_note", length = 500)
    private String resolutionNote;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
