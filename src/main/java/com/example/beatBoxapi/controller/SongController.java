//package com.example.beatBoxapi.controller;
//
//
//import com.example.beatBoxapi.dto.SongListResponse;
//import com.example.beatBoxapi.dto.SongRequest;
//import com.example.beatBoxapi.service.SongService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//@RestController
//@RequestMapping("/api/songs")
//public class SongController {
//
//    private final SongService songService;
//
//    public SongController(SongService songService) {
//        this.songService = songService;
//    }
//
////    @PostMapping("/add")
//      @PostMapping // NEW: Correct path is /api/songs
//    public ResponseEntity<?> addSong(@RequestPart("request") String requestString,
//                                     @RequestPart("audio")MultipartFile audioFile,
//                                     @RequestPart("image")MultipartFile imageFile)
//    {
//        try
//        {
//            ObjectMapper objectMapper=new ObjectMapper();
//            SongRequest  songRequest = objectMapper.readValue(requestString, SongRequest.class);
//            songRequest.setImageFile(imageFile);
//            songRequest.setAudioFile(audioFile);
//
////             songService.addSong(songRequest);
////            return ResponseEntity.status(HttpStatus.CREATED).body(songService.addSong(songRequest));
//
//            Object addedSongResponse = songService.addSong(songRequest);
//            return ResponseEntity.status(HttpStatus.CREATED).body(addedSongResponse);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            return  ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//
//    @GetMapping
//   public ResponseEntity<?> listSongs()
//    {
//        try
//        {
//          return ResponseEntity.ok(songService.getAllSongs());
//        } catch (Exception e) {
//            return  ResponseEntity.ok(new SongListResponse(false,null));
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> removeSong(@PathVariable String id){
//        try
//        {
//          boolean removed =  songService.removeSong(id);
//          if(removed)
//          {
//              return  ResponseEntity.noContent().build();
//          }
//          else
//          {
//             return ResponseEntity.badRequest().build();
//          }
//
//        } catch (Exception e) {
//            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }
//
//}
package com.example.beatBoxapi.controller;


import com.example.beatBoxapi.dto.SongListResponse;
import com.example.beatBoxapi.dto.SongRequest;
import com.example.beatBoxapi.service.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    // FIX: Changed 'ADMIN' to 'ROLE_ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addSong(@RequestPart("request") String requestString,
                                     @RequestPart("audio")MultipartFile audioFile,
                                     @RequestPart("image")MultipartFile imageFile)
    {
        try
        {
            ObjectMapper objectMapper=new ObjectMapper();
            SongRequest  songRequest = objectMapper.readValue(requestString, SongRequest.class);
            songRequest.setImageFile(imageFile);
            songRequest.setAudioFile(audioFile);

            Object addedSongResponse = songService.addSong(songRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedSongResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    // FIX: Changed 'USER', 'ADMIN' to 'ROLE_USER', 'ROLE_ADMIN'
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> listSongs()
    {
        try
        {
            return ResponseEntity.ok(songService.getAllSongs());
        } catch (Exception e) {
            return  ResponseEntity.ok(new SongListResponse(false,null));
        }
    }

    @DeleteMapping("/{id}")
    // FIX: Changed 'ADMIN' to 'ROLE_ADMIN'
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> removeSong(@PathVariable String id){
        try
        {
            boolean removed =  songService.removeSong(id);
            if(removed)
            {
                return  ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}