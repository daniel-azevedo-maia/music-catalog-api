package com.danielmaia.musiccatalog.album.controller;

import com.danielmaia.musiccatalog.album.dto.AlbumCreateRequest;
import com.danielmaia.musiccatalog.album.dto.AlbumResponse;
import com.danielmaia.musiccatalog.album.dto.AlbumSearchFilter;
import com.danielmaia.musiccatalog.album.dto.AlbumUpdateRequest;
import com.danielmaia.musiccatalog.album.service.AlbumService;
import com.danielmaia.musiccatalog.common.dto.PageResponse;
import com.danielmaia.musiccatalog.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@Tag(
        name = "Albums",
        description = "Operations for managing albums and their relationship with artists."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/albums")
public class AlbumController {

    private final AlbumService albumService;

    @Operation(
            summary = "Search albums",
            description = "Returns albums using optional filters, pagination and sorting."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Albums returned successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = PageResponse.class)
            )
    )
    @GetMapping
    public ResponseEntity<PageResponse<AlbumResponse>> findAll(
            @Parameter(description = "Text contained in the album title")
            @RequestParam(required = false)
            String title,

            @Parameter(description = "Artist identifier")
            @RequestParam(required = false)
            Long artistId,

            @Parameter(description = "Initial release date in yyyy-MM-dd format")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "Final release date in yyyy-MM-dd format")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @ParameterObject
            @PageableDefault(
                    size = 20,
                    sort = "title",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        AlbumSearchFilter filter = new AlbumSearchFilter(
                title,
                artistId,
                startDate,
                endDate
        );

        PageResponse<AlbumResponse> response = PageResponse.from(
                albumService.findAll(filter, pageable)
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Find album by ID",
            description = "Returns a specific album by its unique identifier."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Album found successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AlbumResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Album not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @GetMapping("/{id}")
    public AlbumResponse findById(@PathVariable Long id) {
        return albumService.findById(id);
    }

    @Operation(
            summary = "Create album",
            description = "Creates a new album associated with an artist."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Album created successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AlbumResponse.class)
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
            description = "Related artist not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Album already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
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

    @Operation(
            summary = "Update album",
            description = "Updates an existing album in the catalog."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Album updated successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = AlbumResponse.class)
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
            description = "Album or related artist not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "409",
            description = "Album already exists or violates a database constraint",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @PutMapping("/{id}")
    public AlbumResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AlbumUpdateRequest request
    ) {
        return albumService.update(id, request);
    }

    @Operation(
            summary = "Delete album",
            description = "Deletes an album from the catalog by its unique identifier."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Album deleted successfully",
            content = @Content
    )
    @ApiResponse(
            responseCode = "404",
            description = "Album not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorResponse.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        albumService.delete(id);

        return ResponseEntity.noContent().build();
    }
}