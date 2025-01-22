package com.storytale.storytaleAI.controller;

import com.storytale.storytaleAI.service.StoryContentService;
import com.storytale.storytaleAI.service.WriterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/API/stories")
public class StoryContentController {

    @Autowired
    private StoryContentService storyContentService;

    @Autowired
    private WriterService writerService;

    @GetMapping("/{storyId}/image/{index}/{storyText}")
    public ResponseEntity<?> generateImage(@PathVariable int storyId, @PathVariable int index, @PathVariable String storyText) {
        try {
            writerService.verifyStoryOwnership(storyId);
            return storyContentService.generateImage(storyId, index, storyText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating image: " + e.getMessage());
        }
    }

    @PostMapping("/{storyId}/content/{index}/{storyText}")
    public ResponseEntity<?> addStoryContent(@PathVariable int storyId, @PathVariable int index, @PathVariable String storyText) {
        try {
            writerService.verifyStoryOwnership(storyId);
            return storyContentService.addStoryContent(storyId, index, storyText);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding story content: " + e.getMessage());
        }
    }

    @DeleteMapping("/{storyId}/section/{index}")
    public ResponseEntity<?> deleteStorySection(@PathVariable int storyId, @PathVariable int index) {
        try {
            writerService.verifyStoryOwnership(storyId);
            return storyContentService.deleteStorySection(storyId, index);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting story section: " + e.getMessage());
        }
    }
}
