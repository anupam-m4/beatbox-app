package com.example.beatBoxapi.service;


import com.example.beatBoxapi.document.User;
import com.example.beatBoxapi.dto.RegisterRequest;
import com.example.beatBoxapi.dto.UserResponse;
import com.example.beatBoxapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest request)
    {
//        if already  email exits
        if(userRepository.existsByEmail(request.getEmail()))
        {
            throw  new RuntimeException("email exits");
        }
//      if not present then create a new
     User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(newUser);

        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .role(UserResponse.Role.USER)
                .build();

    }

    public User findByEmail(String  email)
    {
     return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for the email :"+ email));

    }

    public  User promoteToAdmin(String email)
    {
       User existingUser = findByEmail(email);
       existingUser.setRole(User.Role.ADMIN);
       return  userRepository.save(existingUser);
    }



}
