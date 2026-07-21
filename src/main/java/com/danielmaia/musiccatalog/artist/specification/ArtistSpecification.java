package com.danielmaia.musiccatalog.artist.specification;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public final class ArtistSpecification {

    private ArtistSpecification() {
    }

    public static Specification<Artist> isActive() {
        return (artistRoot, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(artistRoot.get("active"));
    }

    public static Specification<Artist> nameContains(String name) {
        return (artistRoot, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchedName =
                    "%" + name.trim().toLowerCase(Locale.ROOT) + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(artistRoot.get("name")),
                    searchedName
            );
        };
    }

    public static Specification<Artist> countryContains(String country) {
        return (artistRoot, query, criteriaBuilder) -> {
            if (country == null || country.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String searchedCountry =
                    "%" + country.trim().toLowerCase(Locale.ROOT) + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(artistRoot.get("country")),
                    searchedCountry
            );
        };
    }

    public static Specification<Artist> withFilters(
            String name,
            String country
    ) {
        return isActive()
                .and(nameContains(name))
                .and(countryContains(country));
    }
}