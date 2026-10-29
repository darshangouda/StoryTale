package com.storytale.storytaleAI.model;

import java.io.Serializable;
import java.util.Objects;

public class StoryLineId implements Serializable {
    private StoryTale storyTale;
    private int index;

    public StoryLineId() {
    }

    public StoryLineId(StoryTale storyTale, int index) {
        this.storyTale = storyTale;
        this.index = index;
    }

    public StoryTale getStoryTale() {
        return storyTale;
    }

    public void setStoryTale(StoryTale storyTale) {
        this.storyTale = storyTale;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryLineId that = (StoryLineId) o;
        return index == that.index && Objects.equals(storyTale, that.storyTale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storyTale, index);
    }
}
