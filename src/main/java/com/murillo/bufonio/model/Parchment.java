package com.murillo.bufonio.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Parchment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParchment;
    private String bufonioMessage;
    private LocalDateTime createdAt;
    private int urgencyLevel;
    private int commentsCount;
    private String dominantTheme;
    private String frequency;
    private String emotionalTone;
    private String riskLevel;
    private String detectedPatterns;
    private String potentialImpacts;
    private String monitoringIndicator;
    private String bufonioAdvice;
    @ManyToOne(optional = false)
    private Channel channel;
}
