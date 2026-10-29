package com.storytale.storytaleAI.service;

import com.storytale.storytaleAI.model.*;
import com.storytale.storytaleAI.repository.StoryLineRepository;
import com.storytale.storytaleAI.repository.StoryTaleRepository;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//for convert image to base64
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.util.Base64;
//get current user name

@Service
public class StoryContentService {

    @Autowired
    private StoryLineRepository storyLineRepository;

    @Autowired
    private StoryTaleRepository storyTaleRepository;

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private OpenAiImageModel openAiImageModel;
    public ResponseEntity<?> generateImage(int storyId, int index, String imagePrompt) {
        try {
            StoryTale storyTale = storyTaleRepository.findById(storyId)
                    .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));
            System.out.println("[ImageGen] storyId=" + storyId + ", index=" + index + ", prompt=" + imagePrompt);

            String base64imageText = generateBase64ImageAsString(imagePrompt);
            if (base64imageText == null || base64imageText.isBlank()) {
                throw new RuntimeException("Image generation failed: no image data returned from OpenAI.");
            }

            StoryLineId storyLineId = new StoryLineId(storyTale, index);
            StoryLine storyLine = storyLineRepository.findById(storyLineId).orElse(new StoryLine());
            storyLine.setIndex(index);
            storyLine.setStoryImage(base64imageText);
            storyLine.setImagePrompt(imagePrompt);
            storyLine.setStoryTale(storyTale);

            storyLineRepository.save(storyLine);

            System.out.println("[ImageGen] Success for storyId=" + storyId + ", index=" + index + ", imageLength=" + base64imageText.length());
            return new ResponseEntity<>(base64imageText, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("[ImageGen] Error generating image for storyId=" + storyId + ", index=" + index + ": " + e.getMessage());
            e.printStackTrace(System.out);
            return new ResponseEntity<>("Error generating image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addStoryContent(int storyId, int index, String storyText) {
        try {
            // Fetch the StoryTale object by storyId
            StoryTale storyTale = storyTaleRepository.findById(storyId)
                    .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));
            // Create a composite key for the StoryLine
            StoryLineId storyLineId = new StoryLineId(storyTale, index);
            // Fetch the StoryLine if it exists
            StoryLine storyLine = storyLineRepository.findById(storyLineId)
                    .orElseThrow(() -> new RuntimeException("Story section not found for index: " + index));
            // Update the storyText without affecting the existing image
            storyLine.setStoryLine(storyText);
            // Save the updated StoryLine
            storyLineRepository.save(storyLine);
            return new ResponseEntity<>("Story content updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating story content: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> deleteStorySection(int storyId, int index) {
        storyLineRepository.deleteByStoryIdAndIndex(storyId, index);
        return ResponseEntity.ok("Story section deleted successfully");
    }
    private String getBase64ImageAsString(String storyText) {
        //Just to get Static Image
        try {
            ClassPathResource imgFile = new ClassPathResource("static/istockphoto.jpg");
            byte[] imageBytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            return base64Image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String generateBase64ImageAsString(String storyText){
        try {
            System.out.println("[ImageGen API] Request prompt: " + storyText);
            OpenAiImageOptions openAiImageOptions=new OpenAiImageOptions();
            openAiImageOptions.setModel("gpt-image-1"); // use a model available for the current API key/account
            openAiImageOptions.setHeight(1024);
            openAiImageOptions.setWidth(1024);
            //openAiImageOptions.setStyle("natural");
            //openAiImageOptions.setN(2); //No. of Images, support only in dall-e-2
            ImagePrompt imagePrompt=new ImagePrompt(storyText,openAiImageOptions);
            System.out.println("[ImageGen API] Sending request to OpenAI image model...");
            ImageResponse imageResponse = openAiImageModel.call(imagePrompt);
            System.out.println("[ImageGen API] Response object: " + imageResponse);

            if (imageResponse != null && imageResponse.getResult() != null && imageResponse.getResult().getOutput() != null) {
                String base64 = imageResponse.getResult().getOutput().getB64Json();
                if (base64 != null && !base64.isBlank()) {
                    System.out.println("[ImageGen API] Received base64 image length: " + base64.length());
                    return base64;
                }

                String imageUrl = imageResponse.getResult().getOutput().getUrl();
                if (imageUrl != null && !imageUrl.isBlank()) {
                    try {
                        byte[] imageBytes = new java.net.URL(imageUrl).openStream().readAllBytes();
                        String encoded = Base64.getEncoder().encodeToString(imageBytes);
                        System.out.println("[ImageGen API] Received URL image length: " + encoded.length());
                        return encoded;
                    } catch (Exception urlException) {
                        System.out.println("[ImageGen API] Failed to download image URL: " + urlException.getMessage());
                        urlException.printStackTrace(System.out);
                    }
                }
            }

            System.out.println("[ImageGen API] No image data returned from OpenAI response.");
            return null;
        }
        catch (Exception e) {
            System.out.println("[ImageGen API] Error: " + e.getMessage());
            e.printStackTrace(System.out);
            return null;
        }
    }

}

