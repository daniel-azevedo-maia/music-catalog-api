package com.danielmaia.musiccatalog.artist.service;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistServiceImplTest {

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    @Test
    @DisplayName("Should create artist when name does not exist")
    void shouldCreateArtistWhenNameDoesNotExist() {
        ArtistCreateRequest request = new ArtistCreateRequest(
                "Radiohead",
                "English rock band.",
                "United Kingdom"
        );

        when(artistRepository.existsByNameIgnoreCase("Radiohead")).thenReturn(false);
        when(artistRepository.save(any(Artist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArtistResponse response = artistService.create(request);

        assertThat(response.name()).isEqualTo("Radiohead");
        assertThat(response.biography()).isEqualTo("English rock band.");
        assertThat(response.country()).isEqualTo("United Kingdom");
        assertThat(response.active()).isTrue();

        verify(artistRepository).existsByNameIgnoreCase("Radiohead");
        verify(artistRepository).save(any(Artist.class));
    }

    @Test
    @DisplayName("Should throw exception when artist already exists")
    void shouldThrowExceptionWhenArtistAlreadyExists() {
        ArtistCreateRequest request = new ArtistCreateRequest(
                "Radiohead",
                "English rock band.",
                "United Kingdom"
        );

        when(artistRepository.existsByNameIgnoreCase("Radiohead")).thenReturn(true);

        assertThatThrownBy(() -> artistService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Artist already exists");

        verify(artistRepository).existsByNameIgnoreCase("Radiohead");
        verify(artistRepository, never()).save(any(Artist.class));
    }

    @Test
    @DisplayName("Should find artist by id")
    void shouldFindArtistById() {
        Artist artist = new Artist("Daft Punk", "French electronic music duo.", "France");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        ArtistResponse response = artistService.findById(1L);

        assertThat(response.name()).isEqualTo("Daft Punk");
        assertThat(response.biography()).isEqualTo("French electronic music duo.");
        assertThat(response.country()).isEqualTo("France");
        assertThat(response.active()).isTrue();

        verify(artistRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when artist is not found")
    void shouldThrowExceptionWhenArtistIsNotFound() {
        when(artistRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> artistService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Artist not found");

        verify(artistRepository).findById(99L);
    }

    @Test
    @DisplayName("Should list all active artists")
    void shouldListAllActiveArtists() {
        Artist daftPunk = new Artist("Daft Punk", null, "France");
        Artist radiohead = new Artist("Radiohead", null, "United Kingdom");

        when(artistRepository.findAllByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(daftPunk, radiohead));

        List<ArtistResponse> response = artistService.findAllActive();

        assertThat(response)
                .extracting(ArtistResponse::name)
                .containsExactly("Daft Punk", "Radiohead");

        verify(artistRepository).findAllByActiveTrueOrderByNameAsc();
    }

    @Test
    @DisplayName("Should update artist")
    void shouldUpdateArtist() {
        Artist artist = new Artist("Old Name", "Old biography.", "Old country");

        ArtistUpdateRequest request = new ArtistUpdateRequest(
                "New Name",
                "New biography.",
                "New country"
        );

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        ArtistResponse response = artistService.update(1L, request);

        assertThat(response.name()).isEqualTo("New Name");
        assertThat(response.biography()).isEqualTo("New biography.");
        assertThat(response.country()).isEqualTo("New country");

        verify(artistRepository).findById(1L);
    }

    @Test
    @DisplayName("Should deactivate artist")
    void shouldDeactivateArtist() {
        Artist artist = new Artist("Radiohead", null, "United Kingdom");

        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        artistService.deactivate(1L);

        assertThat(artist.isActive()).isFalse();

        verify(artistRepository).findById(1L);
    }
}