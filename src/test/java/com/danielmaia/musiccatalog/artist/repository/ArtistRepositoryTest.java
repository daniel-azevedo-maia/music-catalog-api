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
import com.danielmaia.musiccatalog.artist.specification.ArtistSpecification;
import org.springframework.data.domain.Sort;

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
    @DisplayName("Should find only active artists using specification")
    void shouldFindOnlyActiveArtistsUsingSpecification() {

        Artist radiohead = new Artist(
          "Radiohead",
          null,
          "United Kingdom"
        );

        Artist daftPunk = new Artist(
                "Daft Punk",
                null,
                "France"
        );

        Artist inactiveArtist = new Artist(
                "Inactive Artist",
                null,
                "Brazil"
        );

        inactiveArtist.deactivate();

        artistRepository.saveAllAndFlush(
                List.of(radiohead, daftPunk, inactiveArtist)
        );

        List<Artist> activeArtists = artistRepository.findAll(
                ArtistSpecification.isActive(),
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(activeArtists)
                .extracting(Artist::getName)
                .containsExactly("Daft Punk", "Radiohead");


    }

    @Test
    @DisplayName("Should find artists whose name contains the searched text")
    void shouldFindArtistsWhoseNameContainsTheSearchedText() {
        Artist queen = new Artist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist queensOfTheStoneAge = new Artist(
                "Queens of the Stone Age",
                null,
                "United States"
        );

        Artist radiohead = new Artist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        artistRepository.saveAllAndFlush(
                List.of(queen, queensOfTheStoneAge, radiohead)
        );

        List<Artist> artists = artistRepository.findAll(
                ArtistSpecification.nameContains("QUEEN"),
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(artists)
                .extracting(Artist::getName)
                .containsExactly(
                        "Queen",
                        "Queens of the Stone Age"
                );
    }

    @Test
    @DisplayName("Should find only active artists whose name contains the searched text")
    void shouldFindOnlyActiveArtistsWhoseNameContainsTheSearchedText() {
        Artist queen = new Artist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist inactiveQueen = new Artist(
                "Queen Tribute",
                null,
                "Brazil"
        );

        inactiveQueen.deactivate();

        Artist radiohead = new Artist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        artistRepository.saveAllAndFlush(
                List.of(queen, inactiveQueen, radiohead)
        );

        var specification = ArtistSpecification.isActive()
                .and(ArtistSpecification.nameContains("queen"));

        List<Artist> artists = artistRepository.findAll(
                specification,
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(artists)
                .extracting(Artist::getName)
                .containsExactly("Queen");
    }

    @Test
    @DisplayName("Should find artists whose country contains the searched text")
    void shouldFindArtistsWhoseCountryContainsTheSearchedText() {
        Artist queen = new Artist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist radiohead = new Artist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        Artist daftPunk = new Artist(
                "Daft Punk",
                null,
                "France"
        );

        artistRepository.saveAllAndFlush(
                List.of(queen, radiohead, daftPunk)
        );

        List<Artist> artists = artistRepository.findAll(
                ArtistSpecification.countryContains("KINGDOM"),
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(artists)
                .extracting(Artist::getName)
                .containsExactly(
                        "Queen",
                        "Radiohead"
                );
    }

    @Test
    @DisplayName("Should return all active artists when filters are empty")
    void shouldReturnAllActiveArtistsWhenFiltersAreEmpty() {
        Artist radiohead = new Artist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        Artist daftPunk = new Artist(
                "Daft Punk",
                null,
                "France"
        );

        Artist inactiveArtist = new Artist(
                "Inactive Artist",
                null,
                "Brazil"
        );

        inactiveArtist.deactivate();

        artistRepository.saveAllAndFlush(
                List.of(radiohead, daftPunk, inactiveArtist)
        );

        List<Artist> artists = artistRepository.findAll(
                ArtistSpecification.withFilters(null, "  "),
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(artists)
                .extracting(Artist::getName)
                .containsExactly(
                        "Daft Punk",
                        "Radiohead"
                );
    }

    @Test
    @DisplayName("Should find active artists using name and country filters")
    void shouldFindActiveArtistsUsingNameAndCountryFilters() {
        Artist queen = new Artist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist queenLatifah = new Artist(
                "Queen Latifah",
                null,
                "United States"
        );

        Artist inactiveQueen = new Artist(
                "Queen Tribute",
                null,
                "United Kingdom"
        );

        inactiveQueen.deactivate();

        Artist radiohead = new Artist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        artistRepository.saveAllAndFlush(
                List.of(
                        queen,
                        queenLatifah,
                        inactiveQueen,
                        radiohead
                )
        );

        List<Artist> artists = artistRepository.findAll(
                ArtistSpecification.withFilters(
                        "queen",
                        "kingdom"
                ),
                Sort.by(Sort.Direction.ASC, "name")
        );

        assertThat(artists)
                .extracting(Artist::getName)
                .containsExactly("Queen");
    }

}