package com.danielmaia.musiccatalog.album.controller;

import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.service.AlbumService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
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

@WebMvcTest(AlbumController.class)
@ActiveProfiles("test")
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlbumService albumService;

    @Test
    @DisplayName("Should list all albums")
    void shouldListAllAlbums() throws Exception {
        List<AlbumResponse> albums = List.of(
                new AlbumResponse(
                        1L,
                        "A Night at the Opera",
                        LocalDate.of(1975, 11, 21),
                        1L,
                        "Queen",
                        Instant.parse("2026-01-01T10:00:00Z"),
                        Instant.parse("2026-01-01T10:00:00Z")
                ),
                new AlbumResponse(
                        2L,
                        "News of the World",
                        LocalDate.of(1977, 10, 28),
                        1L,
                        "Queen",
                        Instant.parse("2026-01-02T10:00:00Z"),
                        Instant.parse("2026-01-02T10:00:00Z")
                )
        );

        when(albumService.findAll()).thenReturn(albums);

        mockMvc.perform(get("/api/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("A Night at the Opera"))
                .andExpect(jsonPath("$[0].releaseDate").value("1975-11-21"))
                .andExpect(jsonPath("$[0].artistId").value(1L))
                .andExpect(jsonPath("$[0].artistName").value("Queen"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("News of the World"))
                .andExpect(jsonPath("$[1].releaseDate").value("1977-10-28"))
                .andExpect(jsonPath("$[1].artistId").value(1L))
                .andExpect(jsonPath("$[1].artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should find album by id")
    void shouldFindAlbumById() throws Exception {
        AlbumResponse album = new AlbumResponse(
                1L,
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(albumService.findById(1L)).thenReturn(album);

        mockMvc.perform(get("/api/v1/albums/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate").value("1975-11-21"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should create album")
    void shouldCreateAlbum() throws Exception {
        AlbumResponse response = new AlbumResponse(
                1L,
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(albumService.create(any())).thenReturn(response);

        String requestBody = """
                {
                  "title": "A Night at the Opera",
                  "releaseDate": "1975-11-21",
                  "artistId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/albums")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate").value("1975-11-21"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should update album")
    void shouldUpdateAlbum() throws Exception {
        AlbumResponse response = new AlbumResponse(
                1L,
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-02T10:00:00Z")
        );

        when(albumService.update(eq(1L), any())).thenReturn(response);

        String requestBody = """
                {
                  "title": "A Night at the Opera",
                  "releaseDate": "1975-11-21"
                }
                """;

        mockMvc.perform(put("/api/v1/albums/{id}", 1L)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate").value("1975-11-21"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should delete album")
    void shouldDeleteAlbum() throws Exception {
        mockMvc.perform(delete("/api/v1/albums/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(albumService).delete(1L);
    }

    @Test
    @DisplayName("Should return bad request when creating album with blank title")
    void shouldReturnBadRequestWhenCreatingAlbumWithBlankTitle() throws Exception {
        String requestBody = """
                {
                  "title": "",
                  "releaseDate": "1975-11-21",
                  "artistId": 1
                }
                """;

        mockMvc.perform(post("/api/v1/albums")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(albumService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating album without artist id")
    void shouldReturnBadRequestWhenCreatingAlbumWithoutArtistId() throws Exception {
        String requestBody = """
                {
                  "title": "A Night at the Opera",
                  "releaseDate": "1975-11-21"
                }
                """;

        mockMvc.perform(post("/api/v1/albums")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(albumService, never()).create(any());
    }
}
