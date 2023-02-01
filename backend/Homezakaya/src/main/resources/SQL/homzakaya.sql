-- homezakaya DB
CREATE DATABASE if NOT EXISTS homezakaya COLLATE utf8mb4_general_ci;
USE homezakaya;

-- DROP
DROP table IF EXISTS userinroom;
DROP table IF EXISTS friend;
DROP table IF EXISTS room;
DROP table IF EXISTS user;
DROP table IF EXISTS sentence;
DROP table IF EXISTS topic;


-- TABLE User

CREATE TABLE IF NOT EXISTS user (
    userId VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    birthDate VARCHAR(45) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    mannerPoint DOUBLE NULL,
    evaluatedCount INT NULL,
    alcoholPoint DOUBLE NOT NULL,
    username VARCHAR(20) NOT NULL,
    refreshToken VARCHAR(255) NULL,
    PRIMARY KEY (userId)
    );


-- TABLE Friend

CREATE TABLE IF NOT EXISTS friend (
    userAId VARCHAR(45) NOT NULL,
    userBId VARCHAR(45) NOT NULL,
    isConnected BOOLEAN NOT NULL,
    PRIMARY KEY (userAId, userBId),
    FOREIGN KEY (userAId) REFERENCES user (userId) on delete cascade,
    FOREIGN KEY (userBId) REFERENCES user (userId) on delete cascade
    );


-- TABLE Room

CREATE TABLE IF NOT EXISTS room (
    roomId INT AUTO_INCREMENT,
    title VARCHAR(45) NOT NULL,
    password VARCHAR(45) NULL,
    category VARCHAR(45) NOT NULL,
    hostId VARCHAR(45) NOT NULL,
    createdTime DATETIME NOT NULL,
    personLimit INT NOT NULL,
    personCount INT NOT NULL default 0,
    PRIMARY KEY (roomId),
    FOREIGN KEY (hostId) REFERENCES user (userId) on delete cascade
    );


-- TABLE UserInRoom

CREATE TABLE IF NOT EXISTS userinroom (
    userId VARCHAR(45) NOT NULL,
    roomId INT NOT NULL,
    PRIMARY KEY (userId),
    FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE,
    FOREIGN KEY (roomId) REFERENCES room (roomId) ON DELETE CASCADE
    );


-- TABLE Sentence
CREATE TABLE IF NOT EXISTS sentence (
    sentenceId INT AUTO_INCREMENT,
    content VARCHAR(255) NOT NULL,
    PRIMARY KEY (sentenceId)
    );


-- TABLE Topic

CREATE TABLE IF NOT EXISTS topic (
    topicId INT AUTO_INCREMENT,
    content VARCHAR(255) NOT NULL,
    PRIMARY KEY (topicId)
    );