package com.murillo.bufonio.dto.summary;

import java.time.LocalDateTime;

public interface ParchmentSummary {
    Long getIdParchment();
    String getBufonioMessage();
    LocalDateTime getCreatedAt();
    int getUrgencyLevel();
}