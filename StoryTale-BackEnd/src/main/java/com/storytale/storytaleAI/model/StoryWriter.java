package com.storytale.storytaleAI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class StoryWriter {

    @Id
    private String writerName; // Primary key

    private String writerPassword;

    @OneToMany(mappedBy = "storyWriter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryTale> storyTales; // One writer has many story tales

}
