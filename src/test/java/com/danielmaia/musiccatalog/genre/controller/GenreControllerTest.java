package com.danielmaia.musiccatalog.genre.controller;

import com.danielmaia.musiccatalog.genre.dto.GenreResponse;
import com.danielmaia.musiccatalog.genre.service.GenreService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@ActiveProfiles("test")
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    @DisplayName("Should list all genres")
    void shouldListAllGenres() throws Exception {
        List<GenreResponse> genres = List.of(
                new GenreResponse(
                        1L,
                        "Rock",
                        "Music genre characterized by electric guitars, drums and strong rhythm.",
                        Instant.parse("2026-01-01T10:00:00Z"),
                        Instant.parse("2026-01-01T10:00:00Z")
                ),
                new GenreResponse(
                        2L,
                        "Jazz",
                        "Music genre characterized by improvisation and swing.",
                        Instant.parse("2026-01-02T10:00:00Z"),
                        Instant.parse("2026-01-02T10:00:00Z")
                )
        );

        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/api/v1/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Rock"))
                .andExpect(jsonPath("$[0].description").value("Music genre characterized by electric guitars, drums and strong rhythm."))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jazz"))
                .andExpect(jsonPath("$[1].description").value("Music genre characterized by improvisation and swing."));

        verify(genreService).findAll();
    }

    @Test
    @DisplayName("Should find genre by id")
    void shouldFindGenreById() throws Exception {
        GenreResponse genre = new GenreResponse(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm.",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(genreService.findById(1L)).thenReturn(genre);

        mockMvc.perform(get("/api/v1/genres/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rock"))
                .andExpect(jsonPath("$.description").value("Music genre characterized by electric guitars, drums and strong rhythm."));

        verify(genreService).findById(1L);
    }

    @Test
    @DisplayName("Should create genre")
    void shouldCreateGenre() throws Exception {
        GenreResponse response = new GenreResponse(
                1L,
                "Rock",
                "Music genre characterized by electric guitars, drums and strong rhythm.",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(genreService.create(any())).thenReturn(response);

        String requestBody = """
            {
              "name": "Rock",
              "description": "Music genre characterized by electric guitars, drums and strong rhythm."
            }
            """;

        mockMvc.perform(post("/api/v1/genres")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Rock"))
                .andExpect(jsonPath("$.description").value("Music genre characterized by electric guitars, drums and strong rhythm."));

        verify(genreService).create(any());
    }

    @Test
    @DisplayName("Should update genre")
    void shouldUpdateGenre() throws Exception {
        GenreResponse response = new GenreResponse(
                1L,
                "Classic Rock",
                "Rock music from the classic era.",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-02T10:00:00Z")
        );

        when(genreService.update(eq(1L), any())).thenReturn(response);

        String requestBody = """
            {
              "name": "Classic Rock",
              "description": "Rock music from the classic era."
            }
            """;

        mockMvc.perform(put("/api/v1/genres/{id}", 1L)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Classic Rock"))
                .andExpect(jsonPath("$.description").value("Rock music from the classic era."));

        verify(genreService).update(eq(1L), any());
    }

    @Test
    @DisplayName("Should delete genre")
    void shouldDeleteGenre() throws Exception {
        mockMvc.perform(delete("/api/v1/genres/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(genreService).delete(1L);
    }

    @Test
    @DisplayName("Should return bad request when creating genre with blank name")
    void shouldReturnBadRequestWhenCreatingGenreWithBlankName() throws Exception {
        String requestBody = """
            {
              "name": "",
              "description": "Music genre characterized by electric guitars, drums and strong rhythm."
            }
            """;

        mockMvc.perform(post("/api/v1/genres")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(genreService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating genre with too long name")
    void shouldReturnBadRequestWhenCreatingGenreWithTooLongName() throws Exception {
        String requestBody = """
            {
              "name": "This genre name is intentionally too long because it has more than eighty characters total",
              "description": "Music genre characterized by electric guitars, drums and strong rhythm."
            }
            """;

        mockMvc.perform(post("/api/v1/genres")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(genreService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when updating genre with blank name")
    void shouldReturnBadRequestWhenUpdatingGenreWithBlankName() throws Exception {
        String requestBody = """
            {
              "name": "",
              "description": "Rock music from the classic era."
            }
            """;

        mockMvc.perform(put("/api/v1/genres/{id}", 1L)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(genreService, never()).update(eq(1L), any());
    }

}
