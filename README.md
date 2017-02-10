Blackboard Course Combine
=========================


## DDL
```
CREATE TABLE semester (
  PK     INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  name   TEXT,
  active TINYINT(1) DEFAULT FALSE
);
CREATE TABLE course (
  PK       INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  ID       TEXT,
  name     TEXT,
  semester INT,
  FOREIGN KEY (`semester`) REFERENCES semester (`PK`)
);
CREATE TABLE child (
  PK            INT PRIMARY KEY  AUTO_INCREMENT NOT NULL,
  parent_course INT,
  ID            TEXT,
  FOREIGN KEY (`parent_course`) REFERENCES course (`PK`)
);
CREATE TABLE users (
  PK       INT PRIMARY KEY  AUTO_INCREMENT NOT NULL,
  username TEXT,
  password TEXT,
  email    TEXT
);

```
