package com.storytale.storytaleAI.model;

import jakarta.persistence.*;


@Entity
@IdClass(StoryLineId.class)
public class StoryLine {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyTale_id")
    @Id
    private StoryTale storyTale;

    @Id
    private int index; // Part of composite key

    @Lob
    @Column(columnDefinition = "TEXT") // Allows storing large text data
    @Basic(fetch = FetchType.EAGER)
    private String storyImage;

    private String storyLine;
    private String imagePrompt;

    public StoryTale getStoryTale() {
        return storyTale;
    }

    public int getIndex() {
        return index;
    }

    public String getStoryImage() {
        return storyImage;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public String getImagePrompt() {
        return imagePrompt;
    }

    public void setStoryTale(StoryTale storyTale) {
        this.storyTale = storyTale;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setStoryImage(String storyImage) {
        this.storyImage = storyImage;
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public void setImagePrompt(String imagePrompt) {
        this.imagePrompt = imagePrompt;
    }
}

