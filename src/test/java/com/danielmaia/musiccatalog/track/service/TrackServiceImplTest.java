package com.danielmaia.musiccatalog.track.service;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.repository.AlbumRepository;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.track.domain.Track;
import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;
import com.danielmaia.musiccatalog.track.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrackServiceImplTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private TrackServiceImpl trackService;

    @Test
    @DisplayName("Should create track when album exists")
    void shouldCreateTrackWhenAlbumExists() {
        Album album = createAlbumWithArtist();

        TrackCreateRequest request = new TrackCreateRequest(
                "Bohemian Rhapsody",
                354,
                11,
                1L
        );

        when(albumRepository.findById(1L)).thenReturn(Optional.of(album));

        when(trackRepository.save(any(Track.class))).thenAnswer(invocation -> {
            Track track = invocation.getArgument(0);
            ReflectionTestUtils.setField(track, "id", 1L);
            return track;
        });

        TrackResponse response = trackService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Bohemian Rhapsody");
        assertThat(response.durationSeconds()).isEqualTo(354);
        assertThat(response.trackNumber()).isEqualTo(11);
        assertThat(response.albumId()).isEqualTo(1L);
        assertThat(response.albumTitle()).isEqualTo("A Night at the Opera");
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(albumRepository).findById(1L);
        verify(trackRepository).save(any(Track.class));
    }

    @Test
    @DisplayName("Should throw exception when creating track with non-existing album")
    void shouldThrowExceptionWhenCreatingTrackWithNonExistingAlbum() {
        TrackCreateRequest request = new TrackCreateRequest(
                "Bohemian Rhapsody",
                354,
                11,
                99L
        );

        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.create(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Album not found");

        verify(albumRepository).findById(99L);
        verify(trackRepository, never()).save(any(Track.class));
    }

    @Test
    @DisplayName("Should find track by id")
    void shouldFindTrackById() {
        Track track = createTrack();

        when(trackRepository.findById(1L)).thenReturn(Optional.of(track));

        TrackResponse response = trackService.findById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Bohemian Rhapsody");
        assertThat(response.durationSeconds()).isEqualTo(354);
        assertThat(response.trackNumber()).isEqualTo(11);
        assertThat(response.albumId()).isEqualTo(1L);
        assertThat(response.albumTitle()).isEqualTo("A Night at the Opera");
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(trackRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when track is not found")
    void shouldThrowExceptionWhenTrackIsNotFound() {
        when(trackRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Track not found");

        verify(trackRepository).findById(99L);
    }

    @Test
    @DisplayName("Should list all tracks")
    void shouldListAllTracks() {
        Album album = createAlbumWithArtist();

        Track track1 = new Track(
                "Death on Two Legs",
                223,
                1,
                album
        );

        ReflectionTestUtils.setField(track1, "id", 1L);

        Track track2 = new Track(
                "Bohemian Rhapsody",
                354,
                11,
                album
        );

        ReflectionTestUtils.setField(track2, "id", 2L);

        when(trackRepository.findAll()).thenReturn(List.of(track1, track2));

        List<TrackResponse> response = trackService.findAll();

        assertThat(response)
                .extracting(TrackResponse::title)
                .containsExactly("Death on Two Legs", "Bohemian Rhapsody");

        assertThat(response)
                .extracting(TrackResponse::trackNumber)
                .containsExactly(1, 11);

        verify(trackRepository).findAll();
    }

    @Test
    @DisplayName("Should update track")
    void shouldUpdateTrack() {
        Track track = createTrack();

        TrackUpdateRequest request = new TrackUpdateRequest(
                "Bohemian Rhapsody - Remastered",
                355,
                11
        );

        when(trackRepository.findById(1L)).thenReturn(Optional.of(track));

        TrackResponse response = trackService.update(1L, request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Bohemian Rhapsody - Remastered");
        assertThat(response.durationSeconds()).isEqualTo(355);
        assertThat(response.trackNumber()).isEqualTo(11);
        assertThat(response.albumId()).isEqualTo(1L);
        assertThat(response.albumTitle()).isEqualTo("A Night at the Opera");
        assertThat(response.artistId()).isEqualTo(1L);
        assertThat(response.artistName()).isEqualTo("Queen");

        verify(trackRepository).findById(1L);
    }

    @Test
    @DisplayName("Should delete track")
    void shouldDeleteTrack() {
        Track track = createTrack();

        when(trackRepository.findById(1L)).thenReturn(Optional.of(track));

        trackService.delete(1L);

        verify(trackRepository).findById(1L);
        verify(trackRepository).delete(track);
    }

    private Track createTrack() {
        Album album = createAlbumWithArtist();

        Track track = new Track(
                "Bohemian Rhapsody",
                354,
                11,
                album
        );

        ReflectionTestUtils.setField(track, "id", 1L);

        return track;
    }

    private Album createAlbumWithArtist() {
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

        return album;
    }

}