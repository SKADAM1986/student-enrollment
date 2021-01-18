CREATE TABLE `class_info` (
  `class_id` int NOT NULL AUTO_INCREMENT,
  `class_name` varchar(45) NOT NULL,
  `credits` int NOT NULL,
  PRIMARY KEY (`class_id`);

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

CREATE TABLE `enrollment` (
  `enrollment_id` int NOT NULL,
  `student_id` int NOT NULL,
  `class_id` int NOT NULL,
  `semester_id` int NOT NULL,
  `enrollment_date` date NOT NULL,
  `last_update_date` date DEFAULT NULL,
  PRIMARY KEY (`enrollment_id`,`student_id`,`class_id`,`semester_id`),
  KEY `class_info_ref_idx` (`class_id`),
  KEY `sem_info_ref_idx` (`semester_id`),
  CONSTRAINT `class_info_ref` FOREIGN KEY (`class_id`) REFERENCES `class_info` (`class_id`),
  CONSTRAINT `sem_info_ref` FOREIGN KEY (`semester_id`) REFERENCES `semester` (`sem_id`)

CREATE TABLE `students` (
    `student_id` int NOT NULL,
    `first_name` varchar(45) NOT NULL,
    `last_name` varchar(45) DEFAULT NULL,
    `password` varchar(45) DEFAULT NULL,
    `birth_date` date NOT NULL,
    `create_date` date NOT NULL,
    `phone_no` varchar(45) DEFAULT NULL,
    PRIMARY KEY (`student_id`);

CREATE TABLE `enrollment`.`semester` (
     `sem_id` INT NOT NULL AUTO_INCREMENT,
     `sem_name` VARCHAR(45) NULL,
     `start_date` DATE NULL,
     `end_date` DATE NULL,
     PRIMARY KEY (`sem_id`),
     UNIQUE INDEX `sem_id_UNIQUE` (`sem_id` ASC) VISIBLE,
     UNIQUE INDEX `sem_name_UNIQUE` (`sem_name` ASC) VISIBLE);
