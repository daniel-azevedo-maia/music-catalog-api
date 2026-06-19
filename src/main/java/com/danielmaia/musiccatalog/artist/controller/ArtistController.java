package com.danielmaia.musiccatalog.artist.controller;

import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public List<ArtistResponse> findAllActive() {
        return artistService.findAllActive();
    }
}