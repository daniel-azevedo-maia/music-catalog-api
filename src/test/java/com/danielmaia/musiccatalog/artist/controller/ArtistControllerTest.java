package com.danielmaia.musiccatalog.artist.controller;

import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.service.ArtistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistController.class)
@ActiveProfiles("test")
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistService artistService;

    @Test
    @DisplayName("Should list active artists with pagination")
    void shouldListActiveArtistsWithPagination() throws Exception {
        List<ArtistResponse> artists = List.of(
                new ArtistResponse(
                        1L,
                        "Daft Punk",
                        "French electronic music duo.",
                        "France",
                        true,
                        Instant.parse("2026-01-01T10:00:00Z"),
                        Instant.parse("2026-01-01T10:00:00Z")
                ),
                new ArtistResponse(
                        2L,
                        "Radiohead",
                        "English rock band.",
                        "United Kingdom",
                        true,
                        Instant.parse("2026-01-02T10:00:00Z"),
                        Instant.parse("2026-01-02T10:00:00Z")
                )
        );

        Page<ArtistResponse> artistPage = new PageImpl<>(
                artists,
                PageRequest.of(0, 20),
                2
        );

        when(artistService.findAllActive(any(Pageable.class))).thenReturn(artistPage);

        mockMvc.perform(get("/api/v1/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Daft Punk"))
                .andExpect(jsonPath("$.content[0].country").value("France"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].name").value("Radiohead"))
                .andExpect(jsonPath("$.content[1].country").value("United Kingdom"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(20))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.empty").value(false));
    }

    @Test
    @DisplayName("Should find artist by id")
    void shouldFindArtistById() throws Exception {
        ArtistResponse artist = new ArtistResponse(
                1L,
                "Radiohead",
                "English rock band.",
                "United Kingdom",
                true,
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(artistService.findById(1L)).thenReturn(artist);

        mockMvc.perform(get("/api/v1/artists/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Radiohead"))
                .andExpect(jsonPath("$.country").value("United Kingdom"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("Should create artist")
    void shouldCreateArtist() throws Exception {
        ArtistResponse response = new ArtistResponse(
                1L,
                "Radiohead",
                "English rock band.",
                "United Kingdom",
                true,
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(artistService.create(any())).thenReturn(response);

        String requestBody = """
            {
              "name": "Radiohead",
              "biography": "English rock band.",
              "country": "United Kingdom"
            }
            """;

        mockMvc.perform(post("/api/v1/artists")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Radiohead"))
                .andExpect(jsonPath("$.country").value("United Kingdom"));
    }

    @Test
    @DisplayName("Should update artist")
    void shouldUpdateArtist() throws Exception {
        ArtistResponse response = new ArtistResponse(
                1L,
                "Radiohead Updated",
                "Updated biography.",
                "United Kingdom",
                true,
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-02T10:00:00Z")
        );

        when(artistService.update(eq(1L), any())).thenReturn(response);

        String requestBody = """
            {
              "name": "Radiohead Updated",
              "biography": "Updated biography.",
              "country": "United Kingdom"
            }
            """;

        mockMvc.perform(put("/api/v1/artists/{id}", 1L)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Radiohead Updated"))
                .andExpect(jsonPath("$.biography").value("Updated biography."))
                .andExpect(jsonPath("$.country").value("United Kingdom"));
    }

    @Test
    @DisplayName("Should deactivate artist")
    void shouldDeactivateArtist() throws Exception {
        mockMvc.perform(delete("/api/v1/artists/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}