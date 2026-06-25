package com.danielmaia.musiccatalog.track.controller;

import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;
import com.danielmaia.musiccatalog.track.service.TrackService;
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
@RequestMapping("/api/v1/tracks")
public class TrackController {

    private final TrackService trackService;

    @GetMapping
    public List<TrackResponse> findAll() {
        return trackService.findAll();
    }

    @GetMapping("/{id}")
    public TrackResponse findById(@PathVariable Long id) {
        return trackService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TrackResponse> create(@Valid @RequestBody TrackCreateRequest request) {
        TrackResponse response = trackService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public TrackResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TrackUpdateRequest request
    ) {
        return trackService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trackService.delete(id);

        return ResponseEntity.noContent().build();
    }

}