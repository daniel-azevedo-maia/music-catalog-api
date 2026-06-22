package com.danielmaia.musiccatalog.track.controller;

import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.service.TrackService;
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

@WebMvcTest(TrackController.class)
@ActiveProfiles("test")
class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrackService trackService;

    @Test
    @DisplayName("Should list all tracks")
    void shouldListAllTracks() throws Exception {
        List<TrackResponse> tracks = List.of(
                new TrackResponse(
                        1L,
                        "Death on Two Legs",
                        223,
                        1,
                        1L,
                        "A Night at the Opera",
                        1L,
                        "Queen",
                        Instant.parse("2026-01-01T10:00:00Z"),
                        Instant.parse("2026-01-01T10:00:00Z")
                ),
                new TrackResponse(
                        2L,
                        "Bohemian Rhapsody",
                        354,
                        11,
                        1L,
                        "A Night at the Opera",
                        1L,
                        "Queen",
                        Instant.parse("2026-01-02T10:00:00Z"),
                        Instant.parse("2026-01-02T10:00:00Z")
                )
        );

        when(trackService.findAll()).thenReturn(tracks);

        mockMvc.perform(get("/api/v1/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Death on Two Legs"))
                .andExpect(jsonPath("$[0].durationSeconds").value(223))
                .andExpect(jsonPath("$[0].trackNumber").value(1))
                .andExpect(jsonPath("$[0].albumId").value(1L))
                .andExpect(jsonPath("$[0].albumTitle").value("A Night at the Opera"))
                .andExpect(jsonPath("$[0].artistId").value(1L))
                .andExpect(jsonPath("$[0].artistName").value("Queen"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Bohemian Rhapsody"))
                .andExpect(jsonPath("$[1].durationSeconds").value(354))
                .andExpect(jsonPath("$[1].trackNumber").value(11))
                .andExpect(jsonPath("$[1].albumId").value(1L))
                .andExpect(jsonPath("$[1].albumTitle").value("A Night at the Opera"))
                .andExpect(jsonPath("$[1].artistId").value(1L))
                .andExpect(jsonPath("$[1].artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should find track by id")
    void shouldFindTrackById() throws Exception {
        TrackResponse track = new TrackResponse(
                1L,
                "Bohemian Rhapsody",
                354,
                11,
                1L,
                "A Night at the Opera",
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(trackService.findById(1L)).thenReturn(track);

        mockMvc.perform(get("/api/v1/tracks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Bohemian Rhapsody"))
                .andExpect(jsonPath("$.durationSeconds").value(354))
                .andExpect(jsonPath("$.trackNumber").value(11))
                .andExpect(jsonPath("$.albumId").value(1L))
                .andExpect(jsonPath("$.albumTitle").value("A Night at the Opera"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should create track")
    void shouldCreateTrack() throws Exception {
        TrackResponse response = new TrackResponse(
                1L,
                "Bohemian Rhapsody",
                354,
                11,
                1L,
                "A Night at the Opera",
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-01T10:00:00Z")
        );

        when(trackService.create(any())).thenReturn(response);

        String requestBody = """
            {
              "title": "Bohemian Rhapsody",
              "durationSeconds": 354,
              "trackNumber": 11,
              "albumId": 1
            }
            """;

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Bohemian Rhapsody"))
                .andExpect(jsonPath("$.durationSeconds").value(354))
                .andExpect(jsonPath("$.trackNumber").value(11))
                .andExpect(jsonPath("$.albumId").value(1L))
                .andExpect(jsonPath("$.albumTitle").value("A Night at the Opera"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should update track")
    void shouldUpdateTrack() throws Exception {
        TrackResponse response = new TrackResponse(
                1L,
                "Bohemian Rhapsody - Remastered",
                355,
                11,
                1L,
                "A Night at the Opera",
                1L,
                "Queen",
                Instant.parse("2026-01-01T10:00:00Z"),
                Instant.parse("2026-01-02T10:00:00Z")
        );

        when(trackService.update(eq(1L), any())).thenReturn(response);

        String requestBody = """
            {
              "title": "Bohemian Rhapsody - Remastered",
              "durationSeconds": 355,
              "trackNumber": 11
            }
            """;

        mockMvc.perform(put("/api/v1/tracks/{id}", 1L)
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Bohemian Rhapsody - Remastered"))
                .andExpect(jsonPath("$.durationSeconds").value(355))
                .andExpect(jsonPath("$.trackNumber").value(11))
                .andExpect(jsonPath("$.albumId").value(1L))
                .andExpect(jsonPath("$.albumTitle").value("A Night at the Opera"))
                .andExpect(jsonPath("$.artistId").value(1L))
                .andExpect(jsonPath("$.artistName").value("Queen"));
    }

    @Test
    @DisplayName("Should delete track")
    void shouldDeleteTrack() throws Exception {
        mockMvc.perform(delete("/api/v1/tracks/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(trackService).delete(1L);
    }

    @Test
    @DisplayName("Should return bad request when creating track with blank title")
    void shouldReturnBadRequestWhenCreatingTrackWithBlankTitle() throws Exception {
        String requestBody = """
            {
              "title": "",
              "durationSeconds": 354,
              "trackNumber": 11,
              "albumId": 1
            }
            """;

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(trackService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating track without album id")
    void shouldReturnBadRequestWhenCreatingTrackWithoutAlbumId() throws Exception {
        String requestBody = """
            {
              "title": "Bohemian Rhapsody",
              "durationSeconds": 354,
              "trackNumber": 11
            }
            """;

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(trackService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating track with invalid duration")
    void shouldReturnBadRequestWhenCreatingTrackWithInvalidDuration() throws Exception {
        String requestBody = """
            {
              "title": "Bohemian Rhapsody",
              "durationSeconds": 0,
              "trackNumber": 11,
              "albumId": 1
            }
            """;

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(trackService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating track with invalid track number")
    void shouldReturnBadRequestWhenCreatingTrackWithInvalidTrackNumber() throws Exception {
        String requestBody = """
            {
              "title": "Bohemian Rhapsody",
              "durationSeconds": 354,
              "trackNumber": 0,
              "albumId": 1
            }
            """;

        mockMvc.perform(post("/api/v1/tracks")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(trackService, never()).create(any());
    }

}