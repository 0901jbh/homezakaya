-- homezakaya DB
CREATE DATABASE if NOT EXISTS homezakaya COLLATE utf8mb4_general_ci;
USE homezakaya;

-- DROP
DROP table IF EXISTS UserInRoom;
DROP table IF EXISTS User;
DROP table IF EXISTS Friend;
DROP table IF EXISTS Room;
DROP table IF EXISTS Sentence;
DROP table IF EXISTS Topic;


-- TABLE User

CREATE TABLE IF NOT EXISTS User (
    userId VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    birthDate VARCHAR(45) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    mannerPointSum DOUBLE NULL,
    evaluatedCount INT NULL,
    alcoholPoint DOUBLE NOT NULL,
    username VARCHAR(20) NOT NULL,
    PRIMARY KEY (userId)
    );


-- TABLE Friend

CREATE TABLE IF NOT EXISTS Friend (
    userAId VARCHAR(45) NOT NULL,
    userBId VARCHAR(45) NOT NULL,
    isConnected BOOLEAN NOT NULL,
    PRIMARY KEY (userAId, userBId),
    FOREIGN KEY (userAId) REFERENCES User (userId) on delete cascade,
    FOREIGN KEY (userBId) REFERENCES User (userId) on delete cascade
    );


-- TABLE Room

CREATE TABLE IF NOT EXISTS Room (
                                    roomId INT NOT NULL,
                                    title VARCHAR(45) NOT NULL,
    password VARCHAR(45) NULL,
    category VARCHAR(45) NOT NULL,
    hostId VARCHAR(45) NOT NULL,
    createdTime TIMESTAMP NOT NULL DEFAULT NOW(),
    personLimit DOUBLE NOT NULL,
    personCount DOUBLE NOT NULL,
    PRIMARY KEY (roomId)
    );


-- TABLE UserInRoom

CREATE TABLE IF NOT EXISTS UserInRoom (
    userId VARCHAR(45) NOT NULL,
    roomId INT NOT NULL,
    PRIMARY KEY (userId),
    FOREIGN KEY (userId)
    REFERENCES User (userId)
    ON DELETE CASCADE,
    FOREIGN KEY (roomId)
    REFERENCES Room (roomId)
    ON DELETE CASCADE
    );


-- TABLE Sentence

CREATE TABLE IF NOT EXISTS Sentence (
                                        sentenceId INT NOT NULL,
                                        content VARCHAR(255) NOT NULL,
    PRIMARY KEY (sentenceId)
    );


-- TABLE Topic

CREATE TABLE IF NOT EXISTS Topic (
                                     topicId INT NOT NULL,
                                     content VARCHAR(255) NOT NULL,
    PRIMARY KEY (topicId)
    );