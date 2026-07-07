package com.danielmaia.musiccatalog.genre.service;

import com.danielmaia.musiccatalog.genre.dto.GenreCreateRequest;
import com.danielmaia.musiccatalog.genre.dto.GenreResponse;
import com.danielmaia.musiccatalog.genre.dto.GenreUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenreService {

    GenreResponse create(GenreCreateRequest request);

    GenreResponse findById(Long id);

    Page<GenreResponse> findAll(Pageable pageable);

    GenreResponse update(Long id, GenreUpdateRequest request);

    void delete(Long id);

}