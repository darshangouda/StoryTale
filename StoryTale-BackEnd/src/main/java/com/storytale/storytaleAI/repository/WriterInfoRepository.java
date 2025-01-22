package com.storytale.storytaleAI.repository;

import com.storytale.storytaleAI.model.WriterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WriterInfoRepository extends JpaRepository<WriterInfo, Integer> {

    Optional<WriterInfo> findByStoryWriterWriterName(String writerName);
    @Query("SELECT new com.storytale.storytaleAI.model.WriterInfo(wi.id,wi.firstName, wi.lastName,wi.email) " +
            "FROM WriterInfo wi WHERE wi.storyWriter.writerName = :writerName")
    Optional<WriterInfo> fetchByStoryWriterWriterName(@Param("writerName") String writerName);
}
