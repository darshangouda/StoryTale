package com.storytale.storytaleAI.repository;
import com.storytale.storytaleAI.model.StoryWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryWriterRepository extends JpaRepository<StoryWriter, String> {
    StoryWriter findByWriterName(String username);
    // Additional custom queries can be added here if needed
}
