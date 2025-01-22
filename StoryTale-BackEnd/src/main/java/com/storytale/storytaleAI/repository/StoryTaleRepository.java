package com.storytale.storytaleAI.repository;
import com.storytale.storytaleAI.model.StoryTale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryTaleRepository extends JpaRepository<StoryTale, Integer> {
    // Custom query to fetch StoryTales by writer name
    @Query("SELECT new com.storytale.storytaleAI.model.StoryTale(st.storyId, st.storyTitle) " +
            "FROM StoryTale st WHERE st.storyWriter.writerName = :writerName")
    List<StoryTale> findStorySummariesByWriterName(@Param("writerName") String writerName);
}
