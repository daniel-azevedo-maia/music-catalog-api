package com.danielmaia.musiccatalog.track.controller;

import com.danielmaia.musiccatalog.common.dto.PageResponse;
import com.danielmaia.musiccatalog.common.exception.ErrorResponse;
import com.danielmaia.musiccatalog.track.dto.TrackCreateRequest;
import com.danielmaia.musiccatalog.track.dto.TrackResponse;
import com.danielmaia.musiccatalog.track.dto.TrackUpdateRequest;
import com.danielmaia.musiccatalog.track.service.TrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
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

@Tag(
        name = "Tracks",
        description = "Operations for managing tracks and their relationship with albums."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracks")
public class TrackController {

    private final TrackService trackService;

    @Operation(
            summary = "List tracks with pagination",
            description = "Returns tracks registered in the catalog using pagination and sorting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tracks returned successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PageResponse.class)
            )
    )
    @GetMapping
    public ResponseEntity<PageResponse<TrackResponse>> findAll(
            @ParameterObject
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(PageResponse.from(trackService.findAll(pageable)));
    }

    @Operation(
            summary = "Find track by ID",
            description = "Returns a specific track by its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Track found successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TrackResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Track not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public TrackResponse findById(@PathVariable Long id) {
        return trackService.findById(id);
    }

    @Operation(
            summary = "Create track",
            description = "Creates a new track associated with an album."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Track created successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TrackResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Related album not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Track already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Update track",
            description = "Updates an existing track in the catalog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Track updated successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = TrackResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request body",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Track or related album not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Track already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public TrackResponse update(
            @PathVariable Long id,
            @Valid @RequestBody TrackUpdateRequest request
    ) {
        return trackService.update(id, request);
    }

    @Operation(
            summary = "Delete track",
            description = "Deletes a track from the catalog by its unique identifier."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Track deleted successfully",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Track not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trackService.delete(id);

        return ResponseEntity.noContent().build();
    }
}