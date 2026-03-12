-- V1__init.sql
SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- =========================
-- TABLE: user
-- =========================
CREATE TABLE user (
                      id_user BIGINT AUTO_INCREMENT NOT NULL,
                      name_user VARCHAR(255),
                      email_user VARCHAR(255),
                      password_user VARCHAR(255),
                      CONSTRAINT pk_user PRIMARY KEY (id_user),
                      CONSTRAINT uk_user_email UNIQUE (email_user)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;


-- =========================
-- TABLE: channel
-- =========================
CREATE TABLE channel (
                         id_channel BIGINT AUTO_INCREMENT NOT NULL,
                         name_channel VARCHAR(255),
                         token_channel VARCHAR(255),
                         created_at DATETIME,
                         user_id_user BIGINT NOT NULL,
                         CONSTRAINT pk_channel PRIMARY KEY (id_channel),
                         CONSTRAINT fk_channel_user
                             FOREIGN KEY (user_id_user)
                                 REFERENCES user(id_user)
                                 ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_channel_user
    ON channel(user_id_user);

CREATE UNIQUE INDEX uk_channel_token
    ON channel(token_channel);


-- =========================
-- TABLE: comment
-- =========================
CREATE TABLE comment (
                         id_comment BIGINT AUTO_INCREMENT NOT NULL,
                         channel_id_channel BIGINT NOT NULL,
                         comment TEXT,
                         created_at DATETIME,
                         processed BOOLEAN DEFAULT FALSE,
                         CONSTRAINT pk_comment PRIMARY KEY (id_comment),
                         CONSTRAINT fk_comment_channel
                             FOREIGN KEY (channel_id_channel)
                                 REFERENCES channel(id_channel)
                                 ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_comment_channel
    ON comment(channel_id_channel);


-- =========================
-- TABLE: parchment
-- =========================
CREATE TABLE parchment (
                           id_parchment BIGINT AUTO_INCREMENT NOT NULL,
                           bufonio_message TEXT,
                           created_at DATETIME,
                           urgency_level INT DEFAULT 0,
                           comments_count INT DEFAULT 0,
                           dominant_theme VARCHAR(255),
                           frequency VARCHAR(255),
                           emotional_tone VARCHAR(255),
                           risk_level VARCHAR(255),
                           monitoring_indicator VARCHAR(255),
                           detected_patterns TEXT,
                           potential_impacts TEXT,
                           bufonio_advice TEXT,
                           channel_id_channel BIGINT NOT NULL,
                           CONSTRAINT pk_parchment PRIMARY KEY (id_parchment),
                           CONSTRAINT fk_parchment_channel
                               FOREIGN KEY (channel_id_channel)
                                   REFERENCES channel(id_channel)
                                   ON DELETE CASCADE
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_parchment_channel
    ON parchment(channel_id_channel);