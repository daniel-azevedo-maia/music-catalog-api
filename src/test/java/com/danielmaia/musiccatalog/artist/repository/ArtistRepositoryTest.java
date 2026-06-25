package com.danielmaia.musiccatalog.artist.repository;

import com.danielmaia.musiccatalog.artist.domain.Artist;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArtistRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @DisplayName("Should persist an artist")
    void shouldPersistArtist() {
        Artist artist = new Artist(
                "Radiohead",
                "English rock band formed in Abingdon, Oxfordshire.",
                "United Kingdom"
        );

        Artist savedArtist = artistRepository.saveAndFlush(artist);

        assertThat(savedArtist.getId()).isNotNull();
        assertThat(savedArtist.getName()).isEqualTo("Radiohead");
        assertThat(savedArtist.getBiography()).isEqualTo("English rock band formed in Abingdon, Oxfordshire.");
        assertThat(savedArtist.getCountry()).isEqualTo("United Kingdom");
        assertThat(savedArtist.isActive()).isTrue();
        assertThat(savedArtist.getCreatedAt()).isNotNull();
        assertThat(savedArtist.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find artist by name ignoring case")
    void shouldFindArtistByNameIgnoringCase() {
        Artist artist = new Artist("Daft Punk", "French electronic music duo.", "France");

        artistRepository.saveAndFlush(artist);

        boolean exists = artistRepository.existsByNameIgnoreCase("daft punk");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should list only active artists ordered by name")
    void shouldListOnlyActiveArtistsOrderedByName() {
        Artist radiohead = new Artist("Radiohead", null, "United Kingdom");
        Artist daftPunk = new Artist("Daft Punk", null, "France");
        Artist inactiveArtist = new Artist("Inactive Artist", null, "Brazil");
        inactiveArtist.deactivate();

        artistRepository.saveAllAndFlush(List.of(radiohead, daftPunk, inactiveArtist));

        List<Artist> activeArtists = artistRepository.findAllByActiveTrueOrderByNameAsc();

        assertThat(activeArtists)
                .extracting(Artist::getName)
                .containsExactly("Daft Punk", "Radiohead");
    }
}