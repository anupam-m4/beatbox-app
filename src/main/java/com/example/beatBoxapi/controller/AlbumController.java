//package com.example.beatBoxapi.controller;
//
//import com.example.beatBoxapi.dto.AlbumListResponse;
//import com.example.beatBoxapi.dto.AlbumRequest;
//import com.example.beatBoxapi.service.AlbumService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/albums")
//@RequiredArgsConstructor
//public class AlbumController {
//
//    private  final AlbumService albumService;
//
//    @PostMapping
//    public ResponseEntity<?> addAlbum(@RequestPart("request") String request, @RequestPart("file")MultipartFile file)
//    {
//        try
//        {
//            ObjectMapper objectMapper=new ObjectMapper();
//            AlbumRequest albumRequest=objectMapper.readValue(request, AlbumRequest.class);
//            albumRequest.setImageFile(file);
//            return ResponseEntity.status(HttpStatus.CREATED).body(albumService.addAlbum(albumRequest));
//        }
//        catch(Exception e)
//        {
//           return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<?> listAlbums(){
//        try{
//         return  ResponseEntity.ok(albumService.getAllAlbums());
//        }
//        catch (Exception e)
//        {
//        return  ResponseEntity.ok(new AlbumListResponse(false,null));
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> removeAlbum(@PathVariable String id) {
//        try {
//            boolean removed = albumService.removeAlbum(id);
//
//            if (removed) {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//            } else {
//                return ResponseEntity.badRequest().body("Album not found or could not be deleted.");
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error deleting album: " + e.getMessage());
//        }
//    }
//
//
//
//}

//
//package com.example.beatBoxapi.controller;
//
//import com.example.beatBoxapi.document.Album; // Added this import
//import com.example.beatBoxapi.dto.AlbumListResponse;
//import com.example.beatBoxapi.dto.AlbumRequest;
//import com.example.beatBoxapi.service.AlbumService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize; // Added this import for security
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/albums")
//@RequiredArgsConstructor
//public class AlbumController {
//
//    private  final AlbumService albumService;
//
//    @PostMapping
////    @PreAuthorize("hasRole('ADMIN')") // Ensure this is secured
//    @PreAuthorize("hasAuthority('ADMIN')") // Changed from hasRole
//    public ResponseEntity<?> addAlbum(@RequestPart("request") String request, @RequestPart("file")MultipartFile file)
//    {
//        try
//        {
//            ObjectMapper objectMapper=new ObjectMapper();
//            AlbumRequest albumRequest=objectMapper.readValue(request, AlbumRequest.class);
//            albumRequest.setImageFile(file);
//            return ResponseEntity.status(HttpStatus.CREATED).body(albumService.addAlbum(albumRequest));
//        }
//        catch(Exception e)
//        {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<?> listAlbums(){
//        try{
//            return  ResponseEntity.ok(albumService.getAllAlbums());
//        }
//        catch (Exception e)
//        {
//            return  ResponseEntity.ok(new AlbumListResponse(false,null));
//        }
//    }
//
//    @DeleteMapping("/{id}")
////    @PreAuthorize("hasRole('ADMIN')") // Ensure this is secured
//    @PreAuthorize("hasAuthority('ADMIN')") // Changed from hasRole
//    public ResponseEntity<?> removeAlbum(@PathVariable String id) {
//        try {
//            boolean removed = albumService.removeAlbum(id);
//
//            if (removed) {
//                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//            } else {
//                return ResponseEntity.badRequest().body("Album not found or could not be deleted.");
//            }
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error deleting album: " + e.getMessage());
//        }
//    }
//
//    // =======================================================
//    // ✅ NEW ENDPOINT: UPDATE ALBUM
//    // =======================================================
//    @PutMapping("/{id}")
////    @PreAuthorize("hasRole('ADMIN')") // Ensure only ADMINs can update
//    @PreAuthorize("hasAuthority('ADMIN')") // Changed from hasRole
//    public ResponseEntity<?> updateAlbum(@PathVariable String id, @RequestBody AlbumRequest albumRequest) {
//        try {
//            Album updatedAlbum = albumService.updateAlbum(id, albumRequest);
//            return ResponseEntity.ok(updatedAlbum);
//        } catch (RuntimeException e) {
//            // Handles 'Album not found' exception thrown by the service
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error updating album: " + e.getMessage());
//        }
//    }
//}

package com.example.beatBoxapi.controller;

import com.example.beatBoxapi.document.Album;
import com.example.beatBoxapi.dto.AlbumListResponse;
import com.example.beatBoxapi.dto.AlbumRequest;
import com.example.beatBoxapi.service.AlbumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private  final AlbumService albumService;

    @PostMapping
    // FIX: Changed 'ADMIN' to 'ROLE_ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addAlbum(@RequestPart("request") String request, @RequestPart("file")MultipartFile file)
    {
        try
        {
            ObjectMapper objectMapper=new ObjectMapper();
            AlbumRequest albumRequest=objectMapper.readValue(request, AlbumRequest.class);
            albumRequest.setImageFile(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(albumService.addAlbum(albumRequest));
        }
        catch(Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    // FIX: Changed 'USER', 'ADMIN' to 'ROLE_USER', 'ROLE_ADMIN'
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> listAlbums(){
        try{
            return  ResponseEntity.ok(albumService.getAllAlbums());
        }
        catch (Exception e)
        {
            return  ResponseEntity.ok(new AlbumListResponse(false,null));
        }
    }

    @DeleteMapping("/{id}")
    // FIX: Changed 'ADMIN' to 'ROLE_ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> removeAlbum(@PathVariable String id) {
        try {
            boolean removed = albumService.removeAlbum(id);

            if (removed) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.badRequest().body("Album not found or could not be deleted.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting album: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // FIX: Changed 'ADMIN' to 'ROLE_ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateAlbum(@PathVariable String id, @RequestBody AlbumRequest albumRequest) {
        try {
            Album updatedAlbum = albumService.updateAlbum(id, albumRequest);
            return ResponseEntity.ok(updatedAlbum);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating album: " + e.getMessage());
        }
    }
}