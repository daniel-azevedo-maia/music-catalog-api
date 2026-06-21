package com.danielmaia.musiccatalog.album.repository;

import com.danielmaia.musiccatalog.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
