package com.danielmaia.musiccatalog.artist.dto;

import java.time.Instant;

public record ArtistResponse(
        Long id,
        String name,
        String biography,
        String country,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}