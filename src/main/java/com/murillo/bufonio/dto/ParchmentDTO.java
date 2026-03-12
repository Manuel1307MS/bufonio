package com.murillo.bufonio.dto;

import java.time.LocalDateTime;

public record ParchmentDTO(
        Long idParchment,
        String bufonioMessage,
        LocalDateTime createdAt,
        int urgencyLevel,
        int commentsCount,
        String dominantTheme,
        String frequency,
        String emotionalTone,
        String riskLevel,
        String detectedPatterns,
        String potentialImpacts,
        String monitoringIndicator,
        String bufonioAdvice
) {
}