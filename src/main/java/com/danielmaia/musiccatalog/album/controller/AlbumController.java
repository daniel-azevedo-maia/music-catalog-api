package com.danielmaia.musiccatalog.album.controller;

import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;
import com.danielmaia.musiccatalog.album.service.AlbumService;
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
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    public List<AlbumResponse> findAll() {
        return albumService.findAll();
    }

    @GetMapping("/{id}")
    public AlbumResponse findById(@PathVariable Long id) {
        return albumService.findById(id);
    }

    @PostMapping
    public ResponseEntity<AlbumResponse> create(@Valid @RequestBody AlbumCreateRequest request) {
        AlbumResponse response = albumService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public AlbumResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request
    ) {
        return albumService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);

        return ResponseEntity.noContent().build();
    }
}