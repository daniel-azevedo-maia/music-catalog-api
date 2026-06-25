package com.danielmaia.musiccatalog.genre.service;

import com.danielmaia.musiccatalog.genre.domain.Genre;
import com.danielmaia.musiccatalog.genre.dto.GenreCreateRequest;
import com.danielmaia.musiccatalog.genre.dto.GenreResponse;
import com.danielmaia.musiccatalog.genre.dto.GenreUpdateRequest;
import com.danielmaia.musiccatalog.genre.mapper.GenreMapper;
import com.danielmaia.musiccatalog.genre.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public GenreResponse create(GenreCreateRequest request) {
        Genre genre = new Genre(
                request.name(),
                request.description()
        );

        Genre savedGenre = genreRepository.save(genre);

        return GenreMapper.toResponse(savedGenre);
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponse findById(Long id) {
        Genre genre = findGenreById(id);

        return GenreMapper.toResponse(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponse> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public GenreResponse update(Long id, GenreUpdateRequest request) {
        Genre genre = findGenreById(id);

        genre.update(
                request.name(),
                request.description()
        );

        return GenreMapper.toResponse(genre);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Genre genre = findGenreById(id);

        genreRepository.delete(genre);
    }

    private Genre findGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

}
