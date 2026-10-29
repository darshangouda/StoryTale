package com.storytale.storytaleAI.model;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class StoryTale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storyId; // Primary key

    private String storyTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_name", nullable = false)
    private StoryWriter storyWriter; // Foreign key to StoryWriter

    @OneToMany(mappedBy = "storyTale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StoryLine> storyLines; // One StoryTale has many StoryLines

    public StoryTale() {
    }

    public StoryTale(Integer storyId, String storyTitle) {
        this.storyId = storyId;
        this.storyTitle = storyTitle;
    }

    public Integer getStoryId() {
        return storyId;
    }

    public String getStoryTitle() {
        return storyTitle;
    }

    public StoryWriter getStoryWriter() {
        return storyWriter;
    }

    public List<StoryLine> getStoryLines() {
        return storyLines;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public void setStoryWriter(StoryWriter storyWriter) {
        this.storyWriter = storyWriter;
    }

    public void setStoryLines(List<StoryLine> storyLines) {
        this.storyLines = storyLines;
    }
}