package com.storytale.storytaleAI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

