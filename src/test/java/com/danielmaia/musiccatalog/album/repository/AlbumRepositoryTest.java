package com.danielmaia.musiccatalog.album.repository;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.specification.AlbumSpecification;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AlbumRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    @DisplayName("Should persist an album linked to an artist")
    void shouldPersistAlbumLinkedToArtist() {
        Artist queen = saveArtist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        Album album = new Album(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                queen
        );

        Album savedAlbum = albumRepository.saveAndFlush(album);

        assertThat(savedAlbum.getId()).isNotNull();
        assertThat(savedAlbum.getTitle()).isEqualTo("A Night at the Opera");
        assertThat(savedAlbum.getReleaseDate())
                .isEqualTo(LocalDate.of(1975, 11, 21));
        assertThat(savedAlbum.getArtist().getId()).isEqualTo(queen.getId());
        assertThat(savedAlbum.getArtist().getName()).isEqualTo("Queen");
        assertThat(savedAlbum.getCreatedAt()).isNotNull();
        assertThat(savedAlbum.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should find albums whose title contains the searched text")
    void shouldFindAlbumsWhoseTitleContainsTheSearchedText() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "Opera Omnia",
                        LocalDate.of(1980, 1, 1),
                        queen
                ),
                new Album(
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        queen
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.titleContains("OPERA"),
                Sort.by(Sort.Direction.ASC, "title")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly(
                        "A Night at the Opera",
                        "Opera Omnia"
                );
    }

    @Test
    @DisplayName("Should find albums by artist id")
    void shouldFindAlbumsByArtistId() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist radiohead = saveArtist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        queen
                ),
                new Album(
                        "OK Computer",
                        LocalDate.of(1997, 5, 21),
                        radiohead
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.hasArtistId(queen.getId()),
                Sort.by(Sort.Direction.ASC, "title")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly(
                        "A Night at the Opera",
                        "News of the World"
                );
    }

    @Test
    @DisplayName("Should find albums released from the informed date")
    void shouldFindAlbumsReleasedFromTheInformedDate() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist radiohead = saveArtist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        queen
                ),
                new Album(
                        "OK Computer",
                        LocalDate.of(1997, 5, 21),
                        radiohead
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.releasedFrom(
                        LocalDate.of(1977, 10, 28)
                ),
                Sort.by(Sort.Direction.ASC, "releaseDate")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly(
                        "News of the World",
                        "OK Computer"
                );
    }

    @Test
    @DisplayName("Should find albums released until the informed date")
    void shouldFindAlbumsReleasedUntilTheInformedDate() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist radiohead = saveArtist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        queen
                ),
                new Album(
                        "OK Computer",
                        LocalDate.of(1997, 5, 21),
                        radiohead
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.releasedUntil(
                        LocalDate.of(1977, 10, 28)
                ),
                Sort.by(Sort.Direction.ASC, "releaseDate")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly(
                        "A Night at the Opera",
                        "News of the World"
                );
    }

    @Test
    @DisplayName("Should find albums using all filters")
    void shouldFindAlbumsUsingAllFilters() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist imagineDragons = saveArtist(
                "Imagine Dragons",
                null,
                "United States"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "A Kind of Magic",
                        LocalDate.of(1986, 6, 2),
                        queen
                ),
                new Album(
                        "Night Visions",
                        LocalDate.of(2012, 9, 4),
                        imagineDragons
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.withFilters(
                        "night",
                        queen.getId(),
                        LocalDate.of(1970, 1, 1),
                        LocalDate.of(1980, 12, 31)
                ),
                Sort.by(Sort.Direction.ASC, "title")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly("A Night at the Opera");
    }

    @Test
    @DisplayName("Should return all albums when filters are empty")
    void shouldReturnAllAlbumsWhenFiltersAreEmpty() {
        Artist queen = saveArtist(
                "Queen",
                null,
                "United Kingdom"
        );

        Artist radiohead = saveArtist(
                "Radiohead",
                null,
                "United Kingdom"
        );

        saveAlbums(
                new Album(
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        queen
                ),
                new Album(
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        queen
                ),
                new Album(
                        "OK Computer",
                        LocalDate.of(1997, 5, 21),
                        radiohead
                )
        );

        List<Album> albums = albumRepository.findAll(
                AlbumSpecification.withFilters(
                        null,
                        null,
                        null,
                        null
                ),
                Sort.by(Sort.Direction.ASC, "title")
        );

        assertThat(albums)
                .extracting(Album::getTitle)
                .containsExactly(
                        "A Night at the Opera",
                        "News of the World",
                        "OK Computer"
                );
    }

    private Artist saveArtist(
            String name,
            String biography,
            String country
    ) {
        Artist artist = new Artist(
                name,
                biography,
                country
        );

        return artistRepository.saveAndFlush(artist);
    }

    private void saveAlbums(Album... albums) {
        albumRepository.saveAllAndFlush(List.of(albums));
    }
}