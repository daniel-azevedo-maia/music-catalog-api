package com.danielmaia.musiccatalog.album.mapper;

import com.danielmaia.musiccatalog.album.domain.Album;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;

public final class AlbumMapper {

    private AlbumMapper() {
    }

    public static AlbumResponse toResponse(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getTitle(),
                album.getReleaseDate(),
                album.getArtist().getId(),
                album.getArtist().getName(),
                album.getCreatedAt(),
                album.getUpdatedAt()
        );
    }
}