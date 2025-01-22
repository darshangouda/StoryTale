package com.storytale.storytaleAI.service;
import com.storytale.storytaleAI.model.*;

import com.storytale.storytaleAI.repository.StoryTaleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WriterService {

    @Autowired
    private StoryTaleRepository storyTaleRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public void verifyStoryOwnership(int storyId) {
        String writerName = currentUserService.getCurrentUsername();
        StoryTale storyTale = storyTaleRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));

        if (!storyTale.getStoryWriter().getWriterName().equals(writerName)) {
            throw new RuntimeException("You are not the owner of this story");
        }
    }
}

