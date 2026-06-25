package com.danielmaia.musiccatalog.genre.dto;

import java.time.Instant;

public record GenreResponse(
        Long id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
}
