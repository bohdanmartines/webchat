CREATE TABLE IF NOT EXISTS users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS chats (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    created_by      BIGINT       NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS chat_participants (
    chat_id         BIGINT NOT NULL,
    user_id         BIGINT NOT NULL,
    PRIMARY KEY (chat_id, user_id),
    FOREIGN KEY (chat_id) REFERENCES chats (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);