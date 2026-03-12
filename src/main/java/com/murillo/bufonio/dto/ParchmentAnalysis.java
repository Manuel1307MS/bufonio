package com.murillo.bufonio.dto;

public record ParchmentAnalysis(

        String bufonioMessage,
        int urgencyLevel,
        String dominantTheme,
        String frequency,
        String emotionalTone,
        String riskLevel,
        String detectedPatterns,
        String potentialImpacts,
        String monitoringIndicator,
        String bufonioAdvice
) {}