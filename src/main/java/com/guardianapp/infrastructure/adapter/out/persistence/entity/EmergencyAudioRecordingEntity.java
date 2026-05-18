package com.guardianapp.infrastructure.adapter.out.persistence.entity;

import com.guardianapp.domain.enums.EmergencyAudioStatus;
import com.guardianapp.domain.enums.EmergencyAudioStorageProvider;
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
 * JPA entity for emergency audio recordings.
 */
@Entity
@Table(name = "emergency_audio_recordings", indexes = {
        @Index(name = "idx_emergency_audio_alert", columnList = "emergency_alert_id,audio_recording_created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyAudioRecordingEntity {

    @Id
    @Column(name = "audio_recording_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "emergency_alert_id", nullable = false)
    private UUID emergencyAlertId;

    @ManyToOne
    @JoinColumn(name = "emergency_alert_id", insertable = false, updatable = false)
    private EmergencyAlertEntity emergencyAlert;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_provider", nullable = false, length = 30)
    private EmergencyAudioStorageProvider storageProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "audio_recording_status", nullable = false, length = 20)
    private EmergencyAudioStatus status;

    @Column(name = "storage_file_id", length = 255)
    private String storageFileId;

    @Column(name = "playback_url", length = 2048)
    private String playbackUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "audio_recording_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
