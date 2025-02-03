USE hyperativadb;

CREATE TABLE IF NOT EXISTS users (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

INSERT INTO users (id, username, password)
VALUES (1, 'Stenio', '$2a$10$L7aoDvtL9Ncm08MAZR0WZOu2CLLVTbRe/lM/UrXTQes5aqTVEJ7iG')
    ON DUPLICATE KEY UPDATE username = username;