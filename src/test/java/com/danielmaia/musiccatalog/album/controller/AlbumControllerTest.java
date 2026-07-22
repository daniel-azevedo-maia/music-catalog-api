package com.danielmaia.musiccatalog.album.controller;

import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumSearchFilter;
import com.danielmaia.musiccatalog.album.service.AlbumService;
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
    @DisplayName("Should search albums using filters and pagination")
    void shouldSearchAlbumsUsingFiltersAndPagination() throws Exception {
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

        Page<AlbumResponse> albumPage = new PageImpl<>(
                albums,
                PageRequest.of(0, 20),
                2
        );

        AlbumSearchFilter expectedFilter = new AlbumSearchFilter(
                "night",
                1L,
                LocalDate.of(1970, 1, 1),
                LocalDate.of(1980, 12, 31)
        );

        when(albumService.findAll(
                eq(expectedFilter),
                any(Pageable.class)
        )).thenReturn(albumPage);

        mockMvc.perform(
                        get("/api/v1/albums")
                                .param("title", "night")
                                .param("artistId", "1")
                                .param("startDate", "1970-01-01")
                                .param("endDate", "1980-12-31")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title")
                        .value("A Night at the Opera"))
                .andExpect(jsonPath("$.content[0].releaseDate")
                        .value("1975-11-21"))
                .andExpect(jsonPath("$.content[0].artistId").value(1L))
                .andExpect(jsonPath("$.content[0].artistName")
                        .value("Queen"))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title")
                        .value("News of the World"))
                .andExpect(jsonPath("$.content[1].releaseDate")
                        .value("1977-10-28"))
                .andExpect(jsonPath("$.content[1].artistId").value(1L))
                .andExpect(jsonPath("$.content[1].artistName")
                        .value("Queen"))
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(20))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.empty").value(false));

        verify(albumService).findAll(
                eq(expectedFilter),
                any(Pageable.class)
        );
    }

    @Test
    @DisplayName("Should list all albums when filters are not informed")
    void shouldListAllAlbumsWhenFiltersAreNotInformed() throws Exception {
        Page<AlbumResponse> emptyPage = Page.empty(
                PageRequest.of(0, 20)
        );

        AlbumSearchFilter emptyFilter = new AlbumSearchFilter(
                null,
                null,
                null,
                null
        );

        when(albumService.findAll(
                eq(emptyFilter),
                any(Pageable.class)
        )).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/albums"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.pageNumber").value(0))
                .andExpect(jsonPath("$.pageSize").value(20))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.empty").value(true));

        verify(albumService).findAll(
                eq(emptyFilter),
                any(Pageable.class)
        );
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
                .andExpect(jsonPath("$.title")
                        .value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate")
                        .value("1975-11-21"))
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

        mockMvc.perform(
                        post("/api/v1/albums")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title")
                        .value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate")
                        .value("1975-11-21"))
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

        when(albumService.update(
                eq(1L),
                any()
        )).thenReturn(response);

        String requestBody = """
                {
                  "title": "A Night at the Opera",
                  "releaseDate": "1975-11-21"
                }
                """;

        mockMvc.perform(
                        put("/api/v1/albums/{id}", 1L)
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title")
                        .value("A Night at the Opera"))
                .andExpect(jsonPath("$.releaseDate")
                        .value("1975-11-21"))
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
    void shouldReturnBadRequestWhenCreatingAlbumWithBlankTitle()
            throws Exception {

        String requestBody = """
                {
                  "title": "",
                  "releaseDate": "1975-11-21",
                  "artistId": 1
                }
                """;

        mockMvc.perform(
                        post("/api/v1/albums")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());

        verify(albumService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when creating album without artist id")
    void shouldReturnBadRequestWhenCreatingAlbumWithoutArtistId()
            throws Exception {

        String requestBody = """
                {
                  "title": "A Night at the Opera",
                  "releaseDate": "1975-11-21"
                }
                """;

        mockMvc.perform(
                        post("/api/v1/albums")
                                .contentType("application/json")
                                .content(requestBody)
                )
                .andExpect(status().isBadRequest());

        verify(albumService, never()).create(any());
    }

    @Test
    @DisplayName("Should return bad request when release date filter is invalid")
    void shouldReturnBadRequestWhenReleaseDateFilterIsInvalid()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/albums")
                                .param("startDate", "01-01-1970")
                )
                .andExpect(status().isBadRequest());

        verify(albumService, never()).findAll(
                any(AlbumSearchFilter.class),
                any(Pageable.class)
        );
    }
}