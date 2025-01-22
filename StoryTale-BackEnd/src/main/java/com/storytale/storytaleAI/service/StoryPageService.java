package com.storytale.storytaleAI.service;

import com.storytale.storytaleAI.model.*;
import com.storytale.storytaleAI.repository.StoryTaleRepository;
import com.storytale.storytaleAI.repository.StoryWriterRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class StoryPageService {

    @Autowired
    private StoryTaleRepository storyTaleRepository;

    @Autowired
    private StoryWriterRepository storyWriterRepository;

    @Autowired
    private CurrentUserService currentUserService;

    public ResponseEntity<?> getAllStories() {
        String writerName = currentUserService.getCurrentUsername();
        List<StoryTale> userStories = storyTaleRepository.findStorySummariesByWriterName(writerName);
        return ResponseEntity.ok(userStories);
    }

    public ResponseEntity<?> createOrUpdateStory(int storyId, String userTitle) {
        String writerName = currentUserService.getCurrentUsername();
        StoryWriter writer = storyWriterRepository.findById(writerName)
                .orElseThrow(() -> new RuntimeException("Writer not found with name: " + writerName));

        StoryTale storyTale;
        if (storyId == 0) {
            storyTale = new StoryTale();
            storyTale.setStoryWriter(writer);
        } else {
            storyTale = storyTaleRepository.findById(storyId)
                    .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));
        }
        storyTale.setStoryTitle(userTitle);
        StoryTale savedStoryTale = storyTaleRepository.save(storyTale);
        return ResponseEntity.ok(savedStoryTale.getStoryId());
    }

    public ResponseEntity<?> deleteStory(int storyId) {
        storyTaleRepository.deleteById(storyId);
        return ResponseEntity.ok("Story deleted successfully");
    }

    public ResponseEntity<?> getStoryDetails(int storyId) {
        try {
        StoryTale storyTale = storyTaleRepository.findById(storyId)
                .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));
            // Transform StoryLines to a list of DTOs
            List<StoryLine> storyLines = storyTale.getStoryLines().stream().map(storyLine -> {
                StoryLine story=new StoryLine();
                story.setIndex(storyLine.getIndex());
                story.setStoryImage(storyLine.getStoryImage());
                story.setImagePrompt(storyLine.getImagePrompt());
                story.setStoryLine(storyLine.getStoryLine());
                return story;
            }).toList();
            // Create the response object
            StoryTale response = new StoryTale();
            response.setStoryId(storyTale.getStoryId());
            response.setStoryTitle(storyTale.getStoryTitle());
            response.setStoryLines(storyLines);


            return new ResponseEntity<>(response, HttpStatus.OK);
            //return new ResponseEntity<>(storyTale, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching story details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

