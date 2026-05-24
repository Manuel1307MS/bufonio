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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComment;
    private String comment;
    private LocalDateTime createdAt;
    private boolean processed = false;

    @Column(name = "channel_id_channel", nullable = false)
    private Long channelIdChannel;
}