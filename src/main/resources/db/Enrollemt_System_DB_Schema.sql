CREATE DATABASE `enrollment`;

CREATE TABLE `class_info` (
  `class_name` varchar(45) NOT NULL,
  `credits` int DEFAULT NULL,
  PRIMARY KEY (`class_name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `enrollment` (
  `active_indicator` bit(1) DEFAULT NULL,
  `enrollment_date` date DEFAULT NULL,
  `last_update_date` date DEFAULT NULL,
  `semester_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `class_name` varchar(45) NOT NULL,
  PRIMARY KEY (`class_name`,`semester_id`,`student_id`),
  KEY `FKhhrcemln2sd0mlb7udde3ghcl` (`semester_id`),
  KEY `FKowhsvsa3u070c23mmqdp5mqpy` (`student_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `semester` (
  `sem_id` bigint NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `sem_name` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  PRIMARY KEY (`sem_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `students` (
  `student_id` bigint NOT NULL,
  `create_date` date DEFAULT NULL,
  `birth_date` datetime DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `nationality` varchar(255) DEFAULT NULL,
  `phone_no` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`student_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--Insert statements for class_info table
INSERT INTO class_info (class_name, credits) VALUES ("1A", 4);
INSERT INTO class_info (class_name, credits) VALUES ("1B", 3);
INSERT INTO class_info (class_name, credits) VALUES ("1C", 2);
INSERT INTO class_info (class_name, credits) VALUES ("2A", 4);
INSERT INTO class_info (class_name, credits) VALUES ("2B", 3);
INSERT INTO class_info (class_name, credits) VALUES ("2C", 2);
INSERT INTO class_info (class_name, credits) VALUES ("3A", 4);
INSERT INTO class_info (class_name, credits) VALUES ("3B", 3);
INSERT INTO class_info (class_name, credits) VALUES ("3C", 2);
INSERT INTO class_info (class_name, credits) VALUES ("4A", 4);
INSERT INTO class_info (class_name, credits) VALUES ("4B", 3);
INSERT INTO class_info (class_name, credits) VALUES ("4C", 2);
INSERT INTO class_info (class_name, credits) VALUES ("5A", 4);
INSERT INTO class_info (class_name, credits) VALUES ("5B", 3);
INSERT INTO class_info (class_name, credits) VALUES ("5C", 2);
