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
DROP table IF EXISTS invitefriend;


-- TABLE User

CREATE TABLE IF NOT EXISTS user (
    userId VARCHAR(45) NOT NULL,
    password VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL,
    mannerPoint DOUBLE NOT NULL DEFAULT 0,
    evaluatedCount INT NOT NULL DEFAULT 0,
    alcoholPoint DOUBLE NOT NULL,
    refreshToken VARCHAR(255) NULL,
    state VARCHAR(10) NOT NULL DEFAULT 'offline',
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
    password VARCHAR(45) NOT NULL,
    category VARCHAR(45) NOT NULL,
    hostId VARCHAR(45) NOT NULL,
    personLimit INT NOT NULL,
    personCount INT NOT NULL default 0,
    isPrivate BOOLEAN NOT NULL,
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

-- TABLE invitefriend
CREATE TABLE IF NOT EXISTS invitefriend (
    fromUserId VARCHAR(45) NOT NULL,
    toUserId VARCHAR(45) NOT NULL,
    PRIMARY KEY (fromUserId),
    FOREIGN KEY (fromUserId)
    REFERENCES friend (userAId)
    ON DELETE CASCADE,
    FOREIGN KEY (toUserId)
    REFERENCES friend (userBId)
    ON DELETE CASCADE
    );