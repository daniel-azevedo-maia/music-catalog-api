package com.danielmaia.musiccatalog.track.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TrackCreateRequest(

        @NotBlank(message = "Track title is required")
        @Size(max = 120, message = "Track title must have at most 120 characters")
        String title,

        @NotNull(message = "Track duration is required")
        @Positive(message = "Track duration must be greater than zero")
        Integer durationSeconds,

        @NotNull(message = "Track number is required")
        @Positive(message = "Track number must be greater than zero")
        Integer trackNumber,

        @NotNull(message = "Album id is required")
        Long albumId
) {
}