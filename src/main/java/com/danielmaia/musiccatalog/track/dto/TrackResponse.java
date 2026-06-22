package com.danielmaia.musiccatalog.track.dto;

import java.time.Instant;

public record TrackResponse(
        Long id,
        String title,
        Integer durationSeconds,
        Integer trackNumber,
        Long albumId,
        String albumTitle,
        Long artistId,
        String artistName,
        Instant createdAt,
        Instant updatedAt
) {
}