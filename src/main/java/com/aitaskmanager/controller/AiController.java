package com.aitaskmanager.controller;

import com.aitaskmanager.dto.AiSummarizeRequest;
import com.aitaskmanager.dto.AiSummarizeResponse;
import com.aitaskmanager.service.AiSummarizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiSummarizationService aiSummarizationService;

    public AiController(AiSummarizationService aiSummarizationService) {
        this.aiSummarizationService = aiSummarizationService;
    }

    @PostMapping("/summarize")
    public ResponseEntity<AiSummarizeResponse> summarize(
            @Valid @RequestBody AiSummarizeRequest request) {
        return ResponseEntity.ok(aiSummarizationService.summarize(request));
    }
}
