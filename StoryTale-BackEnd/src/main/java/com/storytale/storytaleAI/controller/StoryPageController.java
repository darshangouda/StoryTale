package com.storytale.storytaleAI.controller;

import com.storytale.storytaleAI.service.StoryPageService;
import com.storytale.storytaleAI.service.WriterService;

import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/API/stories")
public class StoryPageController {

    @Autowired
    private StoryPageService storyPageService;

    @Autowired
    private WriterService writerService;


    private final OpenAiImageModel openAiImageModel;

    public StoryPageController(OpenAiImageModel openAiImageModel) {
        this.openAiImageModel = openAiImageModel;
    }

    @GetMapping
    public ResponseEntity<?> getAllStories() {
        try {
            return storyPageService.getAllStories();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching stories: " + e.getMessage());
        }
    }

    @PostMapping("/{storyId}/title/{userTitle}")
    public ResponseEntity<?> createOrUpdateStory(@PathVariable int storyId, @PathVariable String userTitle) {
        try {
            if(storyId!=0)
                writerService.verifyStoryOwnership(storyId);
            return storyPageService.createOrUpdateStory(storyId, userTitle);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating/updating story: " + e.getMessage());
        }
    }

    @DeleteMapping("/{storyId}")
    public ResponseEntity<?> deleteStory(@PathVariable int storyId) {
        try {
            writerService.verifyStoryOwnership(storyId);
            return storyPageService.deleteStory(storyId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting story: " + e.getMessage());
        }
    }
    @GetMapping("/{storyId}")
    public ResponseEntity<?> getStoryDetails(@PathVariable int storyId) {
        try {
            writerService.verifyStoryOwnership(storyId);
            return storyPageService.getStoryDetails(storyId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching story details: " + e.getMessage());
        }
    }
}
