package com.danielmaia.musiccatalog.track.mapper;

import com.danielmaia.musiccatalog.track.domain.Track;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;

public final class TrackMapper {

    private TrackMapper() {
    }

    public static TrackResponse toResponse(Track track) {
        return new TrackResponse(
                track.getId(),
                track.getTitle(),
                track.getDurationSeconds(),
                track.getTrackNumber(),
                track.getAlbum().getId(),
                track.getAlbum().getTitle(),
                track.getAlbum().getArtist().getId(),
                track.getAlbum().getArtist().getName(),
                track.getCreatedAt(),
                track.getUpdatedAt()
        );
    }

}