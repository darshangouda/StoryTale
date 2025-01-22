package com.storytale.storytaleAI.controller;

import com.storytale.storytaleAI.model.WriterInfo;
import com.storytale.storytaleAI.service.WriterInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/API/profile")
public class WriterInfoController {

    @Autowired
    private WriterInfoService writerInfoService;

    // Get writer information
    @GetMapping
    public ResponseEntity<?> getWriterInfo() {
        try {
            WriterInfo writerInfo = writerInfoService.getWriterInfo();
            return ResponseEntity.ok(writerInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving writer info: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update writer information
    @PutMapping
    public ResponseEntity<?> updateWriterInfo(@RequestBody WriterInfo writerInfo) {
        try {
            WriterInfo updatedInfo = writerInfoService.updateWriterInfo(writerInfo);
            return ResponseEntity.ok(updatedInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating writer info: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

