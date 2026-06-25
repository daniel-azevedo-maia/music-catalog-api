package com.danielmaia.musiccatalog.track.repository;

import com.danielmaia.musiccatalog.track.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
