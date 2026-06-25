package com.danielmaia.musiccatalog.track.service;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.repository.AlbumRepository;
import com.danielmaia.musiccatalog.track.domain.Track;
import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;
import com.danielmaia.musiccatalog.track.mapper.TrackMapper;
import com.danielmaia.musiccatalog.track.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;

    @Override
    @Transactional
    public TrackResponse create(TrackCreateRequest request) {
        Album album = findAlbumById(request.albumId());

        Track track = new Track(
                request.title(),
                request.durationSeconds(),
                request.trackNumber(),
                album
        );

        Track savedTrack = trackRepository.save(track);

        return TrackMapper.toResponse(savedTrack);
    }

    @Override
    @Transactional(readOnly = true)
    public TrackResponse findById(Long id) {
        Track track = findTrackById(id);

        return TrackMapper.toResponse(track);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackResponse> findAll() {
        return trackRepository.findAll().stream()
                .map(TrackMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public TrackResponse update(Long id, TrackUpdateRequest request) {
        Track track = findTrackById(id);

        track.update(
                request.title(),
                request.durationSeconds(),
                request.trackNumber()
        );

        return TrackMapper.toResponse(track);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Track track = findTrackById(id);

        trackRepository.delete(track);
    }

    private Track findTrackById(Long id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Track not found"));
    }

    private Album findAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Album not found"));
    }

}