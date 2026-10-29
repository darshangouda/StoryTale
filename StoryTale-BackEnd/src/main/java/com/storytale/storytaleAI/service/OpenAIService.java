package com.storytale.storytaleAI.service;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.Base64;

@Service
public class OpenAIService {

    @Autowired
    private OpenAiImageModel openAiImageModel;

    private static OpenAiImageOptions getOpenAiImageOptions() {
        OpenAiImageOptions openAiImageOptions=new OpenAiImageOptions();
        openAiImageOptions.setModel("gpt-image-1"); // use a model available for the active API key/account
        openAiImageOptions.setHeight(1024);
        openAiImageOptions.setWidth(1024);
        //openAiImageOptions.setStyle("natural");
        //openAiImageOptions.setN(2); //No. of Images, support only in dall-e-2
        return openAiImageOptions;
    }

    public String generateBase64ImageAsString(String storyText){
        try {
            OpenAiImageOptions openAiImageOptions = getOpenAiImageOptions();
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

    //This is to test Image, without calling OpenAPI.
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
}
