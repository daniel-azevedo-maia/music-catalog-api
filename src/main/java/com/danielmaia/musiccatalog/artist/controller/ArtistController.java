package com.danielmaia.musiccatalog.artist.controller;

import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;
import com.danielmaia.musiccatalog.artist.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/{id}")
    public ArtistResponse findById(@PathVariable Long id) {
        return artistService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ArtistResponse> create(@Valid @RequestBody ArtistCreateRequest request) {
        ArtistResponse response = artistService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ArtistResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ArtistUpdateRequest request
    ) {
        return artistService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        artistService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}