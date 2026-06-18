package com.aitaskmanager.service;

import com.aitaskmanager.dto.AiSummarizeRequest;
import com.aitaskmanager.dto.AiSummarizeResponse;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Simulated AI summarizer. No external API calls are made.
 *
 * <p>It applies a few lightweight heuristics (keyword extraction, length
 * trimming and a templated sentence) to produce a short, human-readable
 * summary. The {@code summarize} method is intentionally isolated behind a
 * single entry point so it can later be swapped for a real LLM call
 * (e.g. OpenAI / Anthropic) without touching the controller.
 */
@Service
public class AiSummarizationService {

    private static final Set<String> STOP_WORDS = new LinkedHashSet<>(Arrays.asList(
            "the", "a", "an", "and", "or", "to", "of", "in", "on", "for",
            "with", "is", "are", "be", "this", "that", "it", "as", "by", "at"));

    public AiSummarizeResponse summarize(AiSummarizeRequest request) {
        String description = request.getDescription() == null ? "" : request.getDescription().trim();

        if (description.isEmpty()) {
            return AiSummarizeResponse.builder()
                    .summary("No description provided to summarize.")
                    .build();
        }

        String keywords = extractKeywords(description);
        String summary = buildSummary(description, keywords);

        return AiSummarizeResponse.builder().summary(summary).build();
    }

    private String extractKeywords(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 3 && !STOP_WORDS.contains(word))
                .distinct()
                .limit(6)
                .collect(Collectors.joining(", "));
    }

    private String buildSummary(String description, String keywords) {
        // Take the first sentence (or whole text) as the core subject.
        String firstSentence = description.split("(?<=[.!?])\\s+")[0];
        String core = capitalize(firstSentence.replaceAll("[.!?]+$", ""));

        StringBuilder sb = new StringBuilder();
        sb.append(core);
        if (!core.endsWith(".")) {
            sb.append(" task");
        }
        sb.append(" requiring");
        if (!keywords.isEmpty()) {
            sb.append(" focus on ").append(keywords);
        } else {
            sb.append(" further definition");
        }
        sb.append(".");

        return sb.toString();
    }

    private String capitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
