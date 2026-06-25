package com.danielmaia.musiccatalog.genre.repository;

import com.danielmaia.musiccatalog.genre.domain.Genre;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("integration-test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("Should persist a genre")
    void shouldPersistGenre() {
        Genre genre = new Genre(
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        Genre savedGenre = genreRepository.saveAndFlush(genre);

        assertThat(savedGenre.getId()).isNotNull();
        assertThat(savedGenre.getName()).isEqualTo("Rock");
        assertThat(savedGenre.getDescription())
                .isEqualTo("Music genre characterized by electric guitars, drums and strong rhythm.");
        assertThat(savedGenre.getCreatedAt()).isNotNull();
        assertThat(savedGenre.getUpdatedAt()).isNotNull();
    }

}
