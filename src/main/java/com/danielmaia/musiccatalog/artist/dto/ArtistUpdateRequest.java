package com.danielmaia.musiccatalog.artist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistUpdateRequest(

        @NotBlank(message = "Artist name is required")
        @Size(max = 120, message = "Artist name must have at most 120 characters")
        String name,

        String biography,

        @Size(max = 80, message = "Country must have at most 80 characters")
        String country

) {
}