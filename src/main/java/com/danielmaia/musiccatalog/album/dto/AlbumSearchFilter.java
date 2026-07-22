package com.danielmaia.musiccatalog.album.dto;

import java.time.LocalDate;

public record AlbumSearchFilter(
        String title,
        Long artistId,
        LocalDate startDate,
        LocalDate endDate
) {
}