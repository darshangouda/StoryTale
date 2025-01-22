package com.storytale.storytaleAI.service;
import com.storytale.storytaleAI.model.StoryWriter;
import com.storytale.storytaleAI.repository.StoryWriterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final StoryWriterRepository storyWriterRepository;


    public CustomUserDetailsService(StoryWriterRepository storyWriterRepository) {
        this.storyWriterRepository = storyWriterRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StoryWriter writer = storyWriterRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
                .username(writer.getWriterName())
                .password(writer.getWriterPassword()) // Use encoded password
                .roles("USER") // Assign roles (you can customize this)
                .build();
    }
}
