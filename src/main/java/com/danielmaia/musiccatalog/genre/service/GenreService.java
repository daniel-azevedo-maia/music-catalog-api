package com.danielmaia.musiccatalog.genre.service;

import com.danielmaia.musiccatalog.genre.dto.GenreCreateRequest;
import com.danielmaia.musiccatalog.genre.dto.GenreResponse;
import com.danielmaia.musiccatalog.genre.dto.GenreUpdateRequest;

import java.util.List;

public interface GenreService {

    GenreResponse create(GenreCreateRequest request);

    GenreResponse findById(Long id);

    List<GenreResponse> findAll();

    GenreResponse update(Long id, GenreUpdateRequest request);

    void delete(Long id);

}
