package com.danielmaia.musiccatalog.album.specification;

import com.danielmaia.musiccatalog.album.domain.Album;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Locale;

public final class AlbumSpecification {

    private AlbumSpecification() {
    }

    public static Specification<Album> titleContains(String title) {
        return (albumRoot, query, criteriaBuilder) -> {
            if (title == null || title.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchedTitle = "%" + title.trim().toLowerCase(Locale.ROOT) + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(albumRoot.get("title")),
                    searchedTitle
            );

        };
    }

    public static Specification<Album> hasArtistId(Long artistId) {
        return (albumRoot, query, criteriaBuilder) -> {
            if (artistId == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    albumRoot.get("artist").get("id"),
                    artistId
            );
        };
    }

    public static Specification<Album> releasedFrom(LocalDate startDate) {
        return (albumRoot, query, criteriaBuilder) -> {
            if (startDate == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.greaterThanOrEqualTo(
                    albumRoot.get("releaseDate"),
                    startDate
            );
        };
    }

    public static Specification<Album> releasedUntil(LocalDate endDate) {
        return (albumRoot, query, criteriaBuilder) -> {
            if (endDate == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.lessThanOrEqualTo(
                    albumRoot.get("releaseDate"),
                    endDate
            );
        };
    }

    public static Specification<Album> withFilters(
            String title,
            Long artistId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return titleContains(title)
                .and(hasArtistId(artistId))
                .and(releasedFrom(startDate))
                .and(releasedUntil(endDate));
    }


}
