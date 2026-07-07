package com.danielmaia.musiccatalog.track.service;

import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrackService {

    TrackResponse create(TrackCreateRequest request);

    TrackResponse findById(Long id);

    Page<TrackResponse> findAll(Pageable pageable);

    TrackResponse update(Long id, TrackUpdateRequest request);

    void delete(Long id);

}