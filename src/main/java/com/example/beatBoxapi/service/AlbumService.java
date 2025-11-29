//package com.example.beatBoxapi.service;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import com.example.beatBoxapi.document.Album;
//import com.example.beatBoxapi.dto.AlbumListResponse;
//import com.example.beatBoxapi.dto.AlbumRequest;
//import com.example.beatBoxapi.repository.AlbumRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class AlbumService {
//
//    private  final AlbumRepository albumRepository;
//    private final Cloudinary cloudinary;
//
//
//    public Album addAlbum(AlbumRequest request) throws IOException {
//       Map<String,Object> imageUploadResult= cloudinary.uploader().upload(request.getImageFile().getBytes(), ObjectUtils.asMap("resource_type","image"));
//
//       Album newAlbum = Album.builder().
//               name(request.getName())
//               .desc(request.getDesc())
//               .bgColor(request.getBgColor())
//               .imageUrl(imageUploadResult.get("secure_url").toString())
//               .build();
//
//      return albumRepository.save(newAlbum);
//    }
//
//    public AlbumListResponse getAllAlbums()
//    {
//      return new AlbumListResponse(true, albumRepository.findAll());
//    }
//
//    public Boolean removeAlbum(String id) {
//     Album existingAlbum = albumRepository.findById(id)
//                .orElseThrow(()->new RuntimeException("Album not found"));
//     albumRepository.delete(existingAlbum);
//
//     return  true;
//    }
//
//
//}


package com.example.beatBoxapi.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.beatBoxapi.document.Album;
import com.example.beatBoxapi.dto.AlbumListResponse;
import com.example.beatBoxapi.dto.AlbumRequest;
import com.example.beatBoxapi.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private  final AlbumRepository albumRepository;
    private final Cloudinary cloudinary;


    public Album addAlbum(AlbumRequest request) throws IOException {
        Map<String,Object> imageUploadResult= cloudinary.uploader().upload(request.getImageFile().getBytes(), ObjectUtils.asMap("resource_type","image"));

        Album newAlbum = Album.builder().
                name(request.getName())
                .desc(request.getDesc())
                .bgColor(request.getBgColor())
                .imageUrl(imageUploadResult.get("secure_url").toString())
                .build();

        return albumRepository.save(newAlbum);
    }

    public AlbumListResponse getAllAlbums()
    {
        return new AlbumListResponse(true, albumRepository.findAll());
    }

    public Boolean removeAlbum(String id) {
        Album existingAlbum = albumRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Album not found"));
        albumRepository.delete(existingAlbum);

        return  true;
    }

    // =======================================================
    // ✅ NEW METHOD: UPDATE ALBUM
    // =======================================================
    public Album updateAlbum(String id, AlbumRequest albumRequest) {
        // 1. Find the existing album
        Album existingAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found with id " + id));

        // 2. Update fields ONLY if the request provides a new value
        if (albumRequest.getName() != null) {
            existingAlbum.setName(albumRequest.getName());
        }
        if (albumRequest.getDesc() != null) {
            existingAlbum.setDesc(albumRequest.getDesc());
        }
        // CRUCIAL: Update the missing bgColor field
        if (albumRequest.getBgColor() != null) {
            existingAlbum.setBgColor(albumRequest.getBgColor());
        }

        // 3. Save the updated album
        return albumRepository.save(existingAlbum);
    }


}
