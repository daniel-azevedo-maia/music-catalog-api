package com.danielmaia.musiccatalog.album.service;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumSearchFilter;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;
import com.danielmaia.musiccatalog.album.repository.AlbumRepository;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceImplTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private AlbumServiceImpl albumService;

    @Test
    @DisplayName("Should create album when artist exists")
    void shouldCreateAlbumWhenArtistExists() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        ReflectionTestUtils.setField(artist, "id", 1L);

        AlbumCreateRequest request = new AlbumCreateRequest(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                1L
        );

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        when(albumRepository.save(any(Album.class))).thenAnswer(invocation -> {
            Album album = invocation.getArgument(0);
            ReflectionTestUtils.setField(album, "id", 1L);
            return album;
        });

        AlbumResponse response = albumService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("A Night at the Opera");
        assertThat(response.releaseDate()).isEqualTo(LocalDate.of(1975, 11, 21));
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(artistRepository).findById(1L);
        verify(albumRepository).save(any(Album.class));
    }

    @Test
    @DisplayName("Should throw exception when creating album with non-existing artist")
    void shouldThrowExceptionWhenCreatingAlbumWithNonExistingArtist() {
        AlbumCreateRequest request = new AlbumCreateRequest(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                99L
        );

        when(artistRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumService.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Artist not found");

        verify(artistRepository).findById(99L);
        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    @DisplayName("Should find album by id")
    void shouldFindAlbumById() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        ReflectionTestUtils.setField(artist, "id", 1L);

        Album album = new Album(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                artist
        );

        ReflectionTestUtils.setField(album, "id", 1L);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        AlbumResponse response = albumService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("A Night at the Opera");
        assertThat(response.releaseDate()).isEqualTo(LocalDate.of(1975, 11, 21));
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(albumRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when album is not found")
    void shouldThrowExceptionWhenAlbumIsNotFound() {
        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> albumService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Album not found");

        verify(albumRepository).findById(99L);
    }

    @Test
    @DisplayName("Should list albums with pagination")
    void shouldListAlbumsWithPagination() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        ReflectionTestUtils.setField(artist, "id", 1L);

        Album album1 = new Album(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                artist
        );

        ReflectionTestUtils.setField(album1, "id", 1L);

        Album album2 = new Album(
                "News of the World",
                LocalDate.of(1977, 10, 28),
                artist
        );

        ReflectionTestUtils.setField(album2, "id", 2L);

        Pageable pageable = PageRequest.of(0, 20);

        Page<Album> albumPage = new PageImpl<>(
                List.of(album1, album2),
                pageable,
                2
        );

        AlbumSearchFilter filter = new AlbumSearchFilter(
                "night",
                1L,
                LocalDate.of(1970, 1, 1),
                LocalDate.of(1980, 12, 31)
        );

        when(albumRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(albumPage);

        Page<AlbumResponse> response =
                albumService.findAll(filter, pageable);

        verify(albumRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }

    @Test
    @DisplayName("Should update album")
    void shouldUpdateAlbum() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        ReflectionTestUtils.setField(artist, "id", 1L);

        Album album = new Album(
                "Old Title",
                LocalDate.of(1970, 1, 1),
                artist
        );

        ReflectionTestUtils.setField(album, "id", 1L);

        AlbumUpdateRequest request = new AlbumUpdateRequest(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21)
        );

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        AlbumResponse response = albumService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("A Night at the Opera");
        assertThat(response.releaseDate()).isEqualTo(LocalDate.of(1975, 11, 21));
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(albumRepository).findById(1L);
    }

    @Test
    @DisplayName("Should delete album")
    void shouldDeleteAlbum() {
        Artist artist = new Artist(
                "Queen",
                "British rock band.",
                "United Kingdom"
        );

        ReflectionTestUtils.setField(artist, "id", 1L);

        Album album = new Album(
                "A Night at the Opera",
                LocalDate.of(1975, 11, 21),
                artist
        );

        ReflectionTestUtils.setField(album, "id", 1L);

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        albumService.delete(1L);

        verify(albumRepository).findById(1L);
        verify(albumRepository).delete(album);
    }
}