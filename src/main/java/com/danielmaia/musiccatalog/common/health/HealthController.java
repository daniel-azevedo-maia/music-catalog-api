package com.danielmaia.musiccatalog.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Health",
        description = "Endpoint for checking API availability."
)
@RestController
public class HealthController {

    @Operation(
            summary = "Check API health status",
            description = "Returns the current health status of the Music Catalog API."
    )
    @GetMapping("/api/v1/health")
    public HealthResponse health() {
        return new HealthResponse("UP");
    }
}