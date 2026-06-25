package com.danielmaia.musiccatalog.genre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreUpdateRequest(

@NotBlank(message = "Genre name is required")
@Size(max = 80, message = "Genre name must have at most 80 characters")
String name,

@Size(max = 255, message = "Genre description must have at most 255 characters")
String description

) {
}
