package com.danielmaia.musiccatalog.genre.repository;

import com.danielmaia.musiccatalog.genre.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
