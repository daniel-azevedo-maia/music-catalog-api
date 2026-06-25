package com.danielmaia.musiccatalog.album.domain;

import com.danielmaia.musiccatalog.artist.domain.Artist;
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
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Entity
@Table(name = "albums")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public Album(String title, LocalDate releaseDate, Artist artist) {
        this.title = requireText(title, "Album title is required");
        this.releaseDate = releaseDate;
        this.artist = Objects.requireNonNull(artist, "Artist is required");
    }

    public void update(String title, LocalDate releaseDate) {
        this.title = requireText(title, "Album title is required");
        this.releaseDate = releaseDate;
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
}
