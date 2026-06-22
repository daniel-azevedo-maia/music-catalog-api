package com.danielmaia.musiccatalog.track.domain;

import com.danielmaia.musiccatalog.album.domain.Album;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

@Getter
@Entity
@Table(name = "tracks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "duration_seconds", nullable = false)
    private Integer durationSeconds;

    @Column(name = "track_number", nullable = false)
    private Integer trackNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Track(String title, Integer durationSeconds, Integer trackNumber, Album album) {
        this.title = requireText(title, "Track title is required");
        this.durationSeconds = requirePositive(durationSeconds, "Track duration must be greater than zero");
        this.trackNumber = requirePositive(trackNumber, "Track number must be greater than zero");
        this.album = Objects.requireNonNull(album, "Album is required");
    }

    public void update(String title, Integer durationSeconds, Integer trackNumber) {
        this.title = requireText(title, "Track title is required");
        this.durationSeconds = requirePositive(durationSeconds, "Track duration must be greater than zero");
        this.trackNumber = requirePositive(trackNumber, "Track number must be greater than zero");
        this.updatedAt = Instant.now();
    }

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    private String requireText(String value, String message) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private Integer requirePositive(Integer value, String message) {
        if (Objects.isNull(value) || value <= 0) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

}