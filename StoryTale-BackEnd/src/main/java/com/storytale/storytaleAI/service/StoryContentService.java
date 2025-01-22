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
            // Generate the image based on storyText
            String base64imageText = generateBase64ImageAsString(imagePrompt);
            // Create a composite key for the StoryLine
            StoryLineId storyLineId = new StoryLineId(storyTale.getStoryId(), index);
            // Check if the StoryLine already exists
            StoryLine storyLine = storyLineRepository.findById(storyLineId).orElse(new StoryLine());
            // Update or create the StoryLine object
            storyLine.setIndex(index);
            storyLine.setStoryImage(base64imageText);
            storyLine.setImagePrompt(imagePrompt);
            storyLine.setStoryTale(storyTale); // Associate with the existing StoryTale

            // Save the updated or new StoryLine
            storyLineRepository.save(storyLine);

            return new ResponseEntity<>(base64imageText, HttpStatus.OK);
        } catch (Exception e) {
            // Handle errors and return an error response
            return new ResponseEntity<>("Error generating image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addStoryContent(int storyId, int index, String storyText) {
        try {
            // Fetch the StoryTale object by storyId
            StoryTale storyTale = storyTaleRepository.findById(storyId)
                    .orElseThrow(() -> new RuntimeException("Story not found with ID: " + storyId));
            // Create a composite key for the StoryLine
            StoryLineId storyLineId = new StoryLineId(storyTale.getStoryId(), index);
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
            OpenAiImageOptions openAiImageOptions=new OpenAiImageOptions(); //.builder().withStyle("vivid").withResponseFormat("b64_json").build();
            openAiImageOptions.setHeight(1024);
            openAiImageOptions.setWidth(1024);
            //openAiImageOptions.setModel("dall-e-2"); //default:dall-e-3
            //openAiImageOptions.setStyle("natural");
            //openAiImageOptions.setN(2); //No. of Images, support only in dall-e-2
            openAiImageOptions.setResponseFormat("b64_json"); // default: url
            ImagePrompt imagePrompt=new ImagePrompt(storyText,openAiImageOptions);
            ImageResponse imageResponse = openAiImageModel.call(imagePrompt);
            return
                    imageResponse.getResult().getOutput().getB64Json();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

