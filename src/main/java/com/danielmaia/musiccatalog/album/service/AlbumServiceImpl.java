package com.danielmaia.musiccatalog.album.service;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumSearchFilter;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;
import com.danielmaia.musiccatalog.album.mapper.AlbumMapper;
import com.danielmaia.musiccatalog.album.repository.AlbumRepository;
import com.danielmaia.musiccatalog.album.specification.AlbumSpecification;
import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    @Override
    @Transactional
    public AlbumResponse create(AlbumCreateRequest request) {
        Artist artist = findArtistById(request.artistId());
        Album album = new Album(request.title(), request.releaseDate(), artist);
        Album savedAlbum = albumRepository.save(album);
        return AlbumMapper.toResponse(savedAlbum);
    }

    @Override
    @Transactional(readOnly = true)
    public AlbumResponse findById(Long id) {
        Album album = findAlbumById(id);
        return AlbumMapper.toResponse(album);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlbumResponse> findAll(
            AlbumSearchFilter filter,
            Pageable pageable
    ) {
        Specification<Album> filters =
                AlbumSpecification.withFilters(
                        filter.title(),
                        filter.artistId(),
                        filter.startDate(),
                        filter.endDate()
                );

        return albumRepository.findAll(filters, pageable)
                .map(AlbumMapper::toResponse);
    }

    @Override
    @Transactional
    public AlbumResponse update(Long id, AlbumUpdateRequest request) {
        Album album = findAlbumById(id);
        album.update(request.title(), request.releaseDate());
        return AlbumMapper.toResponse(album);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Album album = findAlbumById(id);
        albumRepository.delete(album);
    }

    private Album findAlbumById(Long id) {
        return albumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Album not found"));
    }

    private Artist findArtistById(Long id) {
        return artistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Artist not found"));
    }
}