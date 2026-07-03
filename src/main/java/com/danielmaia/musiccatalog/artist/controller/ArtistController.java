package com.danielmaia.musiccatalog.artist.controller;

import com.danielmaia.musiccatalog.artist.dto.ArtistCreateRequest;
import com.danielmaia.musiccatalog.artist.dto.ArtistResponse;
import com.danielmaia.musiccatalog.artist.dto.ArtistUpdateRequest;
import com.danielmaia.musiccatalog.artist.service.ArtistService;
import com.danielmaia.musiccatalog.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        name = "Artists",
        description = "Operations for managing artists registered in the music catalog."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistService artistService;

    @Operation(
            summary = "List active artists",
            description = "Returns all active artists registered in the catalog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Active artists returned successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ArtistResponse.class))
            )
    )
    @GetMapping
    public List<ArtistResponse> findAllActive() {
        return artistService.findAllActive();
    }

    @Operation(
            summary = "Find artist by ID",
            description = "Returns a specific artist by its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Artist found successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArtistResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Artist not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public ArtistResponse findById(@PathVariable Long id) {
        return artistService.findById(id);
    }

    @Operation(
            summary = "Create artist",
            description = "Creates a new artist in the catalog."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Artist created successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArtistResponse.class)
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
            responseCode = "409",
            description = "Artist already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Update artist",
            description = "Updates an existing artist in the catalog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Artist updated successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ArtistResponse.class)
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
            description = "Artist not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Artist already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public ArtistResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ArtistUpdateRequest request
    ) {
        return artistService.update(id, request);
    }

    @Operation(
            summary = "Deactivate artist",
            description = "Deactivates an artist by its unique identifier without physically removing it from the catalog."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Artist deactivated successfully",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Artist not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        artistService.deactivate(id);

        return ResponseEntity.noContent().build();
    }
}