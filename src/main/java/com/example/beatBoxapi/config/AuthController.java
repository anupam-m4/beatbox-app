package com.example.beatBoxapi.config;

import com.example.beatBoxapi.document.User;
import com.example.beatBoxapi.dto.AuthRequest;
import com.example.beatBoxapi.dto.AuthResponse;
import com.example.beatBoxapi.dto.RegisterRequest;
import com.example.beatBoxapi.dto.UserResponse;
import com.example.beatBoxapi.service.AppUserDetailsService;
import com.example.beatBoxapi.service.UserService;
import com.example.beatBoxapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173") // Vite's default port
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public  ResponseEntity<?> login(@RequestBody AuthRequest request)
    {
            try{

//                System.out.println("Login attempt for email: " + request.getEmail());
                User existingUser = userService.findByEmail(request.getEmail());
                if(request.getPortal().equalsIgnoreCase("admin") && existingUser.getRole().name().equalsIgnoreCase("USER"))
                {
                    return ResponseEntity.badRequest().body("Email/Password is incorrect");

                }


                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
                System.out.println("Authentication successful");
                //load user details
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());


                //Generate JWT
                String token = jwtUtil.generateToken(userDetails,existingUser.getRole().name());
//                return ResponseEntity.ok(new AuthResponse(token,request.getEmail(),existingUser.getRole().name()));
                  return ResponseEntity.ok(new AuthResponse(token, userDetails.getUsername(), existingUser.getRole().name()));
            } catch (BadCredentialsException e) {
                System.out.println("Invalid credentials: " + e.getMessage());
                return ResponseEntity.badRequest().body("Email/Password is incorrect");
            }
            catch (Exception e) {

                return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
    }


    @PostMapping("/register")
    public ResponseEntity<?>  register(@RequestBody RegisterRequest request)
    {
       try {
        UserResponse response = userService.registerUser(request);
        return  ResponseEntity.ok(response);
       } catch (RuntimeException e) {
           return  ResponseEntity.badRequest().body(e.getMessage());
       }
       catch (Exception e) {
           return  ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
       }

    }

    @PostMapping("/promote-admin")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- ADD THIS
    public ResponseEntity<?> promoteToAdmin(@RequestBody Map<String,String> request)
    {
        try{
                User user=userService.promoteToAdmin(request.get("email"));
                return  ResponseEntity.ok(new AuthResponse(null,user.getEmail(),"ADMIN"));
        }
        catch (Exception e)
        {
            return  ResponseEntity.badRequest().body("Failed to promote user to Admin");
        }

    }




}
