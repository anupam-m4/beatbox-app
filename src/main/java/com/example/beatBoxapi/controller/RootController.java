package com.example.beatBoxapi.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:5173", "https://beatbox-user.netlify.app"}) 
@RestController
@RequestMapping("/api/health")
public class RootController {

    @GetMapping
    public String healthCheck()
    {
        return  "API Working";
    }

}
