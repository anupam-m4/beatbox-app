package com.example.beatBoxapi.repository;

import com.example.beatBoxapi.document.Album;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<Album,String> {
}
