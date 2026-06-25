package com.danielmaia.musiccatalog.genre.service;

import com.danielmaia.musiccatalog.genre.domain.Genre;
import com.danielmaia.musiccatalog.genre.dto.GenreCreateRequest;
import com.danielmaia.musiccatalog.genre.dto.GenreResponse;
import com.danielmaia.musiccatalog.genre.dto.GenreUpdateRequest;
import com.danielmaia.musiccatalog.genre.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @Test
    @DisplayName("Should create genre")
    void shouldCreateGenre() {
        GenreCreateRequest request = new GenreCreateRequest(
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        when(genreRepository.save(any(Genre.class)))
                .thenAnswer(invocation -> saveGenreWithId(invocation.getArgument(0), 1L));

        GenreResponse response = genreService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Rock");
        assertThat(response.description())
                .isEqualTo("Music genre characterized by electric guitars, drums and strong rhythm.");

        verify(genreRepository).save(any(Genre.class));
    }

    @Test
    @DisplayName("Should find genre by id")
    void shouldFindGenreById() {
        Genre genre = genreWithId(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = genreService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Rock");
        assertThat(response.description())
                .isEqualTo("Music genre characterized by electric guitars, drums and strong rhythm.");

        verify(genreRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when genre is not found")
    void shouldThrowExceptionWhenGenreIsNotFound() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Genre not found");

        verify(genreRepository).findById(99L);
    }

    @Test
    @DisplayName("Should list all genres")
    void shouldListAllGenres() {
        Genre rock = genreWithId(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        Genre jazz = genreWithId(
                2L,
                "Jazz",
                "Music genre characterized by improvisation and swing."
        );

        when(genreRepository.findAll()).thenReturn(List.of(rock, jazz));

        List<GenreResponse> response = genreService.findAll();

        assertThat(response).hasSize(2);

        assertThat(response)
                .extracting(GenreResponse::name)
                .containsExactly("Rock", "Jazz");

        verify(genreRepository).findAll();
    }

    @Test
    @DisplayName("Should update genre")
    void shouldUpdateGenre() {
        Genre genre = genreWithId(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        GenreUpdateRequest request = new GenreUpdateRequest(
                "Classic Rock",
                "Rock music from the classic era."
        );

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        GenreResponse response = genreService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Classic Rock");
        assertThat(response.description()).isEqualTo("Rock music from the classic era.");

        verify(genreRepository).findById(1L);
    }

    @Test
    @DisplayName("Should delete genre")
    void shouldDeleteGenre() {
        Genre genre = genreWithId(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm."
        );

        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));

        genreService.delete(1L);

        verify(genreRepository).findById(1L);
        verify(genreRepository).delete(genre);
    }

    @Test
    @DisplayName("Should not delete genre when genre is not found")
    void shouldNotDeleteGenreWhenGenreIsNotFound() {
        when(genreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> genreService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Genre not found");

        verify(genreRepository).findById(99L);
        verify(genreRepository, never()).delete(any(Genre.class));
    }

    private Genre genreWithId(Long id, String name, String description) {
        Genre genre = new Genre(name, description);
        ReflectionTestUtils.setField(genre, "id", id);
        return genre;
    }

    private Genre saveGenreWithId(Genre genre, Long id) {
        ReflectionTestUtils.setField(genre, "id", id);
        return genre;
    }

}
