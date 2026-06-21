package com.danielmaia.musiccatalog.album.dto;

import java.time.Instant;
import java.time.LocalDate;

public record AlbumResponse(
        Long id,
        String title,
        LocalDate releaseDate,
        Long artistId,
        String artistName,
        Instant createdAt,
        Instant updatedAt) {
}