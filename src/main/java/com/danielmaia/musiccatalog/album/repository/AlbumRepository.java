package com.danielmaia.musiccatalog.album.repository;

import com.danielmaia.musiccatalog.album.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AlbumRepository extends JpaRepository<Album, Long>,
        JpaSpecificationExecutor<Album> {
}
