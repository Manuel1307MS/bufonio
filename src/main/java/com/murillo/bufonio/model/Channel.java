package com.murillo.bufonio.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChannel;
    private String nameChannel;
    private String tokenChannel;
    private LocalDateTime createdAt;

    @Column(name = "user_id_user", nullable = false)
    private Long userIdUser;
}