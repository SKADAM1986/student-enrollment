package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.models.StudentResponse;
import com.blockone.enrollment.service.EnrollmentService;
import com.blockone.enrollment.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StudentController {

    private final Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    StudentService studentService;

    @Autowired
    EnrollmentService enrollmentService;

    /**
     * Gets Student information and Create the record in DB
     * @Param student
     * @return ResponseEntity<StudentResponse>
     */
    @PostMapping(path="/students")
    public ResponseEntity<StudentResponse> createStudent(@RequestBody Student student) {
        log.info("StudentsController.createStudent()");
        //Call service to create Student and return updated Student info
        student.setCreateDate(LocalDate.now());
        //Student Obj saved in DB, send Student id to client
        return new ResponseEntity<StudentResponse>(new StudentResponse(studentService.saveStudent(student).getStudentId(),
                "Student Record Created. Use Get Request to get Student Details"), HttpStatus.CREATED);
    }

    /**
     * Gets Student information and Student Id and Update the record in DB
     * @Param student
     * @Param studentId
     * @return ResponseEntity<StudentResponse>
     */
    @PutMapping(path="/students/{studentId}")
    public ResponseEntity<StudentResponse> modifyStudent(@RequestBody Student student, @PathVariable Long studentId) {
        log.info("StudentsController.modifyStudent()");
        //Call service to modify Student and return updated Student info
        student.setStudentId(studentId);
        //Student Obj saved in DB, send Student id to client
        return new ResponseEntity<StudentResponse>(new StudentResponse(studentService.saveStudent(student).getStudentId(),
                "Student Record Updated. Use Get Request to get Student Details"), HttpStatus.OK);
    }

    /**
     * Get Student Details based on Student Id
     * @Param studentId
     * @return Student
     */
    @GetMapping(path="/students/{studentId}")
    public Student getStudentById(@PathVariable Long studentId) {
        log.info("StudentsController.getStudentById - {}", studentId );
        //Call Service to Get Student by Id
        Student student = studentService.getStudentById(studentId);
        log.info("Student information retrieved - {}", studentId );
        return student;
    }

    /**
     * Get all Students enrolled for Class
     * @Param className
     * @return List<Student>
     */
    @GetMapping(path="/classes/{className}/students")
    public List<Student> getStudentsByClassName(@PathVariable("className") String className) {
        log.info("StudentsController.getStudentsByClassName {}", className);
        //Call EnrollmentService to get all Enrollments for Class
        return studentService.getAllStudentsByClass(className);
    }

    /**
     * Get all Students for Semester
     * @Param semesterId
     * @return List<Student>
     */
    @GetMapping(path="/semesters/{semesterId}/students")
    public List<Student> getStudentsBySemester(@PathVariable("semesterId") Long semesterId) {
        log.info("StudentsController.getStudentsBySemester - {}", semesterId);
        //Call Service Method to get all Enrollments for Semester
        return studentService.getAllStudentsForSemester(semesterId);
    }

    /**
     * Get all Students for Class in Semester
     * @Param className
     * @Param semesterId
     * @return List<Student>
     */
    @GetMapping(path="/semesters/{semesterId}/classes/{className}/students")
    public List<Student> getStudentsForClassInSemester(@PathVariable("className") String className,
                                        @PathVariable("semesterId") Long semesterId) {
        log.info("StudentsController.getStudentsForClassInSemester - className [{}], " +
                "semesterId [{}]", className, semesterId);
        //Get enrollments for class in semester
        return studentService.getAllStudentsForClassInSemester(className, semesterId);
    }
}

