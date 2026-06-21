CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    release_date DATE,
    artist_id BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_albums_artist
    FOREIGN KEY (artist_id)
    REFERENCES artists(id)
);