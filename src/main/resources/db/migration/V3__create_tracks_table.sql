CREATE TABLE tracks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    duration_seconds INTEGER NOT NULL,
    track_number INTEGER NOT NULL,
    album_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_tracks_album
        FOREIGN KEY (album_id)
            REFERENCES albums(id),

    CONSTRAINT ck_tracks_duration_seconds_positive
        CHECK (duration_seconds > 0),

    CONSTRAINT ck_tracks_track_number_positive
        CHECK (track_number > 0),

    CONSTRAINT uk_tracks_album_track_number
        UNIQUE (album_id, track_number)

);