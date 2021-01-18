package com.blockone.enrollment.controllers;

import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.service.ClassService;
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
public class StudentsController {
    private final Logger log = LoggerFactory.getLogger(StudentsController.class);
    @Autowired
    StudentService studentService;

    @Autowired
    EnrollmentService enrollmentService;

    @PostMapping(path="/students")
    public Student createStudent(@RequestBody Student student) {
        log.debug("StudentsController.createStudent() START");
        //Call service to create Student and return updated Student info
        student.setCreateDate(LocalDate.now());
        studentService.saveStudent(student);
        return student;
    }

    @PutMapping(path="/students/{studentId}")
    public Student modifyStudent(@RequestBody Student student, @PathVariable Long studentId) {
        log.debug("StudentsController.modifyStudent() START");
        //Call service to modify Student and return updated Student info
        student.setId(studentId);
        studentService.saveStudent(student);
        return student;
    }

    @GetMapping(path="/students/{studentId}")
    public Student getStudentById(@PathVariable Long studentId) {
        log.debug("StudentsController.getStudentById() START");
        return studentService.getStudentById(studentId);
    }

    @PostMapping(path="/enroll")
    public Enrollment enrollStudent(@RequestBody Enrollment enrollment) throws CreditLimitExceededException{
        log.debug("StudentsController.enrollStudent() START");
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setLastUpdateDate(LocalDate.now());
        enrollment.setActiveIndicator(true);
        return enrollmentService.saveEnrollment(enrollment);
    }

    @PostMapping(path="/withdraw")
    public Enrollment withdrawStudent(@RequestBody Enrollment enrollment) {
        log.debug("StudentsController.withdrawStudent() START");
        enrollment.setLastUpdateDate(LocalDate.now());
        enrollment.setActiveIndicator(false);
        return enrollmentService.saveEnrollment(enrollment);
    }

    @GetMapping(path="/classes/{className}/students")
    public List<Enrollment> getStudentsByClassName(@PathVariable("className") String className) {
        log.debug("StudentsController.getStudentsByClassId() START");
        //TODO - Not working
       return enrollmentService.getAllStudentsByClass(className);
    }

    @GetMapping(path="/semesters/{semesterId}/students")
    public List<Enrollment> getStudentsBySemester(@PathVariable("semesterId") Long semesterId) {
        log.debug("StudentsController.getStudentsByClassId() START");
        return enrollmentService.getAllStudentsForSemester(semesterId);
    }

    @GetMapping(path="/semesters/{semesterId}/classes/{className}/students")
    public List<Enrollment> getStudentsForClassInSemester(@PathVariable("className") String className,
                                        @PathVariable("semesterId") Long semesterId) {
        log.debug("StudentsController.getStudentsForClassInSemester() START");

        //Delete Student from enrollment table
        return enrollmentService.getAllStudentsEnrolledForClassInSemester(className, semesterId);
        //return new Student();
    }



}

