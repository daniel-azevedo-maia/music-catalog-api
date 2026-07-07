package com.danielmaia.musiccatalog.artist.service;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;
import com.danielmaia.musiccatalog.artist.mapper.ArtistMapper;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    @Override
    @Transactional
    public ArtistResponse create(ArtistCreateRequest request) {
        if (artistRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Artist already exists");
        }

        Artist artist = new Artist(
                request.name(),
                request.biography(),
                request.country()
        );

        Artist savedArtist = artistRepository.save(artist);

        return ArtistMapper.toResponse(savedArtist);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtistResponse findById(Long id) {
        Artist artist = findArtistById(id);

        return ArtistMapper.toResponse(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtistResponse> findAllActive(Pageable pageable) {
        return artistRepository.findByActiveTrue(pageable)
                .map(ArtistMapper::toResponse);
    }

    @Override
    @Transactional
    public ArtistResponse update(Long id, ArtistUpdateRequest request) {
        Artist artist = findArtistById(id);

        artist.update(
                request.name(),
                request.biography(),
                request.country()
        );

        return ArtistMapper.toResponse(artist);
    }

    @Override
    @Transactional
    public void deactivate(Long id) {
        Artist artist = findArtistById(id);

        artist.deactivate();
    }

    private Artist findArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found"));
    }
}
