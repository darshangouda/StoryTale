package com.storytale.storytaleAI.controller;
import com.storytale.storytaleAI.security.util.JwtUtil;
import com.storytale.storytaleAI.model.StoryWriter;
import com.storytale.storytaleAI.repository.StoryWriterRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private StoryWriterRepository repository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/home")
    public String home() {
        return "Welcome to the home page!";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody StoryWriter writer) {
        if (repository.findByWriterName(writer.getWriterName()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        writer.setWriterPassword(encoder.encode(writer.getWriterPassword()));
        repository.save(writer);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StoryWriter loginRequest, HttpServletResponse response) {
        System.out.println(loginRequest.getWriterName()+":"+loginRequest.getWriterPassword());
        try {
            // Attempt authentication
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getWriterName(),
                            loginRequest.getWriterPassword()
                    )
            );
            // Generate JWT token upon successful authentication
            String token = jwtUtil.generateToken(authentication);
            // Set JWT token as an HTTP-only cookie
            Cookie cookie = new Cookie("jwtToken", token);
            cookie.setHttpOnly(false);
            cookie.setSecure(false); // Set to true in production with HTTPS
            cookie.setPath("/");
            cookie.setDomain("localhost");
            //cookie.setMaxAge(60);




            // Cookie expires in 7 days
            response.addCookie(cookie);
            String cookieHeader = String.format("%s=%s; HttpOnly; Secure; Path=http://localhost:3000/; SameSite=None",
                    cookie.getName(), cookie.getValue());
            response.addHeader("Set-Cookie", cookieHeader);

            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException ex) {
            // Invalid credentials, return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/API/validatetoken")
    public ResponseEntity<?> validateToken()
    {
       return ResponseEntity.ok("valid Token");
    }
}
