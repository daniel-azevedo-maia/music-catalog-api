package com.danielmaia.musiccatalog.genre.mapper;

import com.danielmaia.musiccatalog.genre.domain.Genre;
import com.danielmaia.musiccatalog.genre.dto.GenreResponse;

public final class GenreMapper {

    private GenreMapper() {
    }

    public static GenreResponse toResponse(Genre genre) {
        return new GenreResponse(
                genre.getId(),
                genre.getName(),
                genre.getDescription(),
                genre.getCreatedAt(),
                genre.getUpdatedAt()
        );
    }

}
