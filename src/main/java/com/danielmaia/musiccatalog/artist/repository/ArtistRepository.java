package com.danielmaia.musiccatalog.artist.repository;

import com.danielmaia.musiccatalog.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsByNameIgnoreCase(String name);

    List<Artist> findAllByActiveTrueOrderByNameAsc();
}