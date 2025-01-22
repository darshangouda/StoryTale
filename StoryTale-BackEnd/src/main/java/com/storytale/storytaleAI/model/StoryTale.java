package com.storytale.storytaleAI.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
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
}