package com.storytale.storytaleAI.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoryLineDTO {
    private Long storyLineId;
    private Integer index;
    private String storyImage;   // LOB fully loaded as String
    private String storyLine;
    private String imagePrompt;
}
