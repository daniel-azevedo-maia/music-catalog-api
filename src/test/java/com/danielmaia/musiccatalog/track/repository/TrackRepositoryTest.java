package com.danielmaia.musiccatalog.track.repository;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.repository.AlbumRepository;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import com.danielmaia.musiccatalog.track.domain.Track;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrackRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @DisplayName("Should persist a track linked to an album")
    void shouldPersistTrackLinkedToAlbum() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        Artist savedArtist = artistRepository.saveAndFlush(artist);

        Album album = new Album(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                savedArtist
        );

        Album savedAlbum = albumRepository.saveAndFlush(album);

        Track track = new Track(
                "Bohemian Rhapsody",
                354,
                11,
                savedAlbum
        );

        Track savedTrack = trackRepository.saveAndFlush(track);

        assertThat(savedTrack.getId()).isNotNull();
        assertThat(savedTrack.getTitle()).isEqualTo("Bohemian Rhapsody");
        assertThat(savedTrack.getDurationSeconds()).isEqualTo(354);
        assertThat(savedTrack.getTrackNumber()).isEqualTo(11);
        assertThat(savedTrack.getAlbum().getId()).isEqualTo(savedAlbum.getId());
        assertThat(savedTrack.getAlbum().getTitle()).isEqualTo("A Night at the Opera");
        assertThat(savedTrack.getCreatedAt()).isNotNull();
        assertThat(savedTrack.getUpdatedAt()).isNotNull();
    }

}