package com.danielmaia.musiccatalog.album.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlbumCreateRequest(
        @NotBlank(message = "Album title is required") @Size(max = 120, message = "Album title must have at most 120 characters") String title,
        LocalDate releaseDate,
        @NotNull(message = "Artist id is required")
        Long artistId) {
}