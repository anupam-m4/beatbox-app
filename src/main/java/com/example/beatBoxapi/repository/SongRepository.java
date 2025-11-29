package com.example.beatBoxapi.repository;

import com.example.beatBoxapi.document.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SongRepository extends MongoRepository<Song, String> {
}
