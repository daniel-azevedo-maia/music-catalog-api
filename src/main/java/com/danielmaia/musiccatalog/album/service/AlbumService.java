package com.danielmaia.musiccatalog.album.service;

import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;

import java.util.List;

public interface AlbumService {
    AlbumResponse create(AlbumCreateRequest request);

    AlbumResponse findById(Long id);

    List<AlbumResponse> findAll();

    AlbumResponse update(Long id, AlbumUpdateRequest request);

    void delete(Long id);
}