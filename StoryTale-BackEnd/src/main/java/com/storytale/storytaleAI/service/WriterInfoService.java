package com.storytale.storytaleAI.service;

import com.storytale.storytaleAI.model.StoryWriter;
import com.storytale.storytaleAI.model.WriterInfo;
import com.storytale.storytaleAI.repository.StoryWriterRepository;
import com.storytale.storytaleAI.repository.WriterInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WriterInfoService {

    @Autowired
    private WriterInfoRepository writerInfoRepository;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private StoryWriterRepository storyWriterRepository;

    // Retrieve writer information
    public WriterInfo getWriterInfo() {
        String writerName = currentUserService.getCurrentUsername();
        return writerInfoRepository.fetchByStoryWriterWriterName(writerName)
                .orElseThrow(() -> new RuntimeException("WriterInfo not found for writerName: " + writerName));
    }

    // Update writer information
    public WriterInfo updateWriterInfo(WriterInfo updatedInfo) {
        String writerName = currentUserService.getCurrentUsername();

        // Fetch the StoryWriter entity from the database
        StoryWriter storyWriter = storyWriterRepository.findById(writerName)
                .orElseThrow(() -> new RuntimeException("StoryWriter not found with name: " + writerName));

        // Try fetching existing WriterInfo
        Optional<WriterInfo> existingInfoOptional = writerInfoRepository.findByStoryWriterWriterName(writerName);

        WriterInfo writerInfo;

        if (existingInfoOptional.isPresent()) {
            // Update existing WriterInfo
            writerInfo = existingInfoOptional.get();
            writerInfo.setFirstName(updatedInfo.getFirstName());
            writerInfo.setLastName(updatedInfo.getLastName());
            writerInfo.setEmail(updatedInfo.getEmail());
        } else {
            // Create a new WriterInfo
            writerInfo = new WriterInfo();
            writerInfo.setFirstName(updatedInfo.getFirstName());
            writerInfo.setLastName(updatedInfo.getLastName());
            writerInfo.setEmail(updatedInfo.getEmail());

            // Associate the WriterInfo with the fetched StoryWriter
            writerInfo.setStoryWriter(storyWriter);
        }
        // Save and return the updated or new WriterInfo
        return writerInfoRepository.save(writerInfo);
    }


}
