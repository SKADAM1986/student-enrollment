# Design a Enrollment system for school students, with self capability to enroll for any class for any semester 

Roles:

Admin

    Admin can create new Semester Record in the Student Enrollment System.
  
    Admin can create new Student record based on student’s request. Once registered Student Id will be generated and shared with Student for future enrollments.
  
Student

    Students should be able to enroll themselves for classes during available semester options.
  
    Students should be able to withdraw enrollment for particular class and Semester
  
Restraints:

    1.	School administrators can create and modify student records but never delete them
  
    2.	Each class a fixed credit/unit.  Some harder classes might be 4 credits while easier ones could be 2 or 3 credits.
  
    3.	Each student is only allowed to be enrolled in a maximum of 20 credits for each semester
  

# Setup Guide

Prerequisit:
  Install JDK 8 - Refer to below link and install Java SE Development Kit 8
  
  https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
  
Download Maven:
  Download maven zip file below url. Extract zip file.
  
  https://maven.apache.org/download.cgi

Install Github
  Install Github to check in your source code
  
  https://desktop.github.com/

Install Docker Client
  Install Docker from below url. If you do not have account you need create account.
  
  https://docs.docker.com/docker-for-windows/install/
  
Install Database MySQL
  Install MySQL workbench
  https://dev.mysql.com/doc/refman/8.0/en/windows-installation.html
  

# Set environment variables

User Variables:

JAVA_HOME - <Your directory structure>\Java\jdk1.8.0_271
  
MAVEN_HOME - <Your directory structure>\apache-maven-3.5.2

Set PATH Variables (If not automatically set):

%MAVEN_HOME%\bin

%JAVA_HOME%\bin

C:\Program Files\Git\cmd

C:\Program Files\Docker\Docker\resources\bin

C:\ProgramData\DockerDesktop\version-bin

# Download Source Code on Local Machine:

Download source code from git-hub web page.

      https://github.com/SKADAM1986/student-enrollment

OR

Travese to directory where you want to download the code

With command line, Execute below command to clone repository on local machine

      https://github.com/SKADAM1986/student-enrollment.git

After clone/download, Verify "student-enrollment" folder with source code is downloaded.

Verify Dockerfile and docker-compose.yml files are downloaded in student-enrollment folder


# Build and Deploy Steps for Spring boot Application on local machine:

Verification: MySQL should be up and running and application properties should be configure with mysql url.

To Build Spring boot application:

mvn clean install

1. Compile Code
2. Run all JUnit tests
3. Add dependencies
4. package jar file

To run Spring boot application:

     Run jar file as Java application :
     
        command: java -jar <jar name>
        
      OR
      
        command: mvn spring-boot:run

# Build and Deploy Steps for Spring boot Application using Docker:

Travese to directory where you want to download the code (student-enrollment)
  
  Run below command:
  
  docker-compose up --build -d
  
  Above command should execute below on Docket container.
  
     1. Download code from Git reposiotory
     2. Build code with maven
     3. Run jar as Java application
     4. Get latest image of MySQL 8.0
     5. Install MySQL in seperate container
     6. Create required database schema as per configuration mentioned in the source code
     
     

# Technical Details



Refer document for test scenarios - attach Test Document

Share endpoints and Request sample payload


DB Information




