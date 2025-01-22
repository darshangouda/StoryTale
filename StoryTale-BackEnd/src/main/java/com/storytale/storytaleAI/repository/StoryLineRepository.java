package com.storytale.storytaleAI.repository;

import com.storytale.storytaleAI.model.StoryLine;
import com.storytale.storytaleAI.model.StoryLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StoryLineRepository extends JpaRepository<StoryLine, StoryLineId> {
    // Custom query to fetch StoryLines by StoryTale ID
    List<StoryLine> findByStoryTale_StoryId(Integer storyId);

    @Modifying
    @Transactional
    @Query("DELETE FROM StoryLine sl WHERE sl.storyTale.storyId = :storyId AND sl.index = :index")
    void deleteByStoryIdAndIndex(@Param("storyId") int storyId, @Param("index") int index);
}
