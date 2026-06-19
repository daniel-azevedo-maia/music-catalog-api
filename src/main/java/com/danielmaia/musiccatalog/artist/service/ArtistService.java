package com.danielmaia.musiccatalog.artist.service;

import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;

import java.util.List;

public interface ArtistService {

    ArtistResponse create(ArtistCreateRequest request);
    ArtistResponse findById(Long id);
    List<ArtistResponse> findAllActive();
    ArtistResponse update(Long id, ArtistUpdateRequest request);
    void deactivate(Long id);

}
