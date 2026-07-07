package com.danielmaia.musiccatalog.artist.service;

import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArtistService {

    ArtistResponse create(ArtistCreateRequest request);
    ArtistResponse findById(Long id);
    Page<ArtistResponse> findAllActive(Pageable pageable);
    ArtistResponse update(Long id, ArtistUpdateRequest request);
    void deactivate(Long id);

}
