package com.danielmaia.musiccatalog.album.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AlbumUpdateRequest(
        @NotBlank(message = "Album title is required")
        @Size(max = 120, message = "Album title must have at most 120 characters")
        String title,
        LocalDate releaseDate) {
}