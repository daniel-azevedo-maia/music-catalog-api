package com.danielmaia.musiccatalog.track.service;

import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;

import java.util.List;

public interface TrackService {

    TrackResponse create(TrackCreateRequest request);

    TrackResponse findById(Long id);

    List<TrackResponse> findAll();

    TrackResponse update(Long id, TrackUpdateRequest request);

    void delete(Long id);

}