package com.storytale.storytaleAI.dto;

import com.storytale.storytaleAI.model.StoryTale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoryWriterDTO {
        private String writerName; // Still kept for backward compatibility
        private String writerPassword;
}
