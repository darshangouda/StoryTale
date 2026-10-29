package com.storytale.storytaleAI.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StoryLineIdTest {

    @Test
    void compositeKeyShouldBeEqualForSameStoryAndIndex() {
        StoryTale storyTale = new StoryTale();
        storyTale.setStoryId(12);

        StoryLineId left = new StoryLineId(storyTale, 3);
        StoryLineId right = new StoryLineId(storyTale, 3);

        assertEquals(left, right);
        assertEquals(left.hashCode(), right.hashCode());
    }
}
