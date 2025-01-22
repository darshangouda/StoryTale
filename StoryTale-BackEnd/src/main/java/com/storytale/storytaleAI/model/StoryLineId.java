package com.storytale.storytaleAI.model;
import lombok.*;

import java.io.Serializable;

//@Getter
//@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class StoryLineId implements Serializable {
    private Integer storyTale; // Use storyTale ID as part of composite key
    private int index;
}
