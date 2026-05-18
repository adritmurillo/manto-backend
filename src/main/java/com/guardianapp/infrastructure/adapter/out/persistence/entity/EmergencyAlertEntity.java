package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.EmergencyAlertStatus;
import com.guardianapp.domain.enums.EmergencyResolutionType;
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
 * JPA entity for emergency alerts.
 */
@Entity
@Table(name = "emergency_alerts", indexes = {
        @Index(name = "idx_emergency_link_id", columnList = "link_id"),
        @Index(name = "idx_emergency_host_status", columnList = "primary_host_user_id,emergency_alert_status"),
        @Index(name = "idx_emergency_protected", columnList = "protected_user_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAlertEntity {

    @Id
    @Column(name = "emergency_alert_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "link_id", nullable = false)
    private UUID linkId;

    @ManyToOne
    @JoinColumn(name = "link_id", insertable = false, updatable = false)
    private LinkEntity link;

    @Column(name = "protected_user_id", nullable = false)
    private UUID protectedUserId;

    @ManyToOne
    @JoinColumn(name = "protected_user_id", insertable = false, updatable = false)
    private UserEntity protectedUser;

    @Column(name = "primary_host_user_id", nullable = false)
    private UUID primaryHostUserId;

    @ManyToOne
    @JoinColumn(name = "primary_host_user_id", insertable = false, updatable = false)
    private UserEntity primaryHostUser;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "emergency_alert_status", nullable = false, length = 20)
    private EmergencyAlertStatus status;

    @Column(name = "emergency_alert_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by_user_id")
    private UUID resolvedByUserId;

    @ManyToOne
    @JoinColumn(name = "resolved_by_user_id", insertable = false, updatable = false)
    private UserEntity resolvedByUser;

    @Enumerated(EnumType.STRING)
    @Column(name = "resolution_type", length = 30)
    private EmergencyResolutionType resolutionType;

    @Column(name = "resolution_note", length = 500)
    private String resolutionNote;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
