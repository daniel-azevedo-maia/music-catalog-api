package com.danielmaia.musiccatalog.album.repository;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
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
class AlbumRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @DisplayName("Should persist an album linked to an artist")
    void shouldPersistAlbumLinkedToArtist() {
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

        assertThat(savedAlbum.getId()).isNotNull();
        assertThat(savedAlbum.getTitle()).isEqualTo("A Night at the Opera");
        assertThat(savedAlbum.getReleaseDate()).isEqualTo(LocalDate.of(1975, 11, 21));
        assertThat(savedAlbum.getArtist().getId()).isEqualTo(savedArtist.getId());
        assertThat(savedAlbum.getArtist().getName()).isEqualTo("Queen");
        assertThat(savedAlbum.getCreatedAt()).isNotNull();
        assertThat(savedAlbum.getUpdatedAt()).isNotNull();
    }
}
