package com.danielmaia.musiccatalog.artist.mapper;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;

public final class ArtistMapper {

    private ArtistMapper() {
    }

    public static ArtistResponse toResponse(Artist artist) {
        return new ArtistResponse(
                artist.getId(),
                artist.getName(),
                artist.getBiography(),
                artist.getCountry(),
                artist.isActive(),
                artist.getCreatedAt(),
                artist.getUpdatedAt()
        );
    }
}