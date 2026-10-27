-- Game Collection Management System
-- Run this file in MySQL Workbench or with: mysql -u root -p < database/game_collection_db.sql

CREATE DATABASE IF NOT EXISTS game_collection_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE game_collection_db;

CREATE TABLE IF NOT EXISTS games (
    game_id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    platform VARCHAR(50) NOT NULL,
    genre VARCHAR(50) NULL,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    status ENUM('Not Played', 'Playing', 'Completed', 'On Hold') NOT NULL DEFAULT 'Not Played',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (game_id)
);

-- Sample data. These rows are inserted only if the table is empty.
INSERT INTO games (title, platform, genre, price, status)
SELECT 'God of War Ragnarok', 'PC', 'Action', 2999.00, 'Playing'
WHERE NOT EXISTS (SELECT 1 FROM games LIMIT 1);

INSERT INTO games (title, platform, genre, price, status)
SELECT 'The Witcher 3', 'PC', 'RPG', 799.00, 'Completed'
WHERE (SELECT COUNT(*) FROM games) = 1;

INSERT INTO games (title, platform, genre, price, status)
SELECT 'Resident Evil 4', 'PC', 'Horror', 2499.00, 'Not Played'
WHERE (SELECT COUNT(*) FROM games) = 2;

-- Verify the data:
SELECT * FROM games ORDER BY game_id;
