package com.danielmaia.musiccatalog.album.service;

import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlbumService {
    AlbumResponse create(AlbumCreateRequest request);

    AlbumResponse findById(Long id);

    Page<AlbumResponse> findAll(Pageable pageable);

    AlbumResponse update(Long id, AlbumUpdateRequest request);

    void delete(Long id);
}