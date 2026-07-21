package com.danielmaia.musiccatalog.artist.repository;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ArtistRepository extends
        JpaRepository<Artist, Long>,
        JpaSpecificationExecutor<Artist> {

    boolean existsByNameIgnoreCase(String name);
}