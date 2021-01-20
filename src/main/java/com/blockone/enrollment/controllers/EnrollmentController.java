package com.blockone.enrollment.controllers;

import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.EnrollmentResponse;
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

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnrollmentController {

    private final Logger log = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    StudentService studentService;

    @Autowired
    EnrollmentService enrollmentService;

    /**
     * This method accepts StudentId, SemesterId and ClassName and
     * withdraw Enrollment for Semester and Class
     * @Param enrollment
     * @return ResponseEntity<EnrollmentResponse>
     * @throws CreditLimitExceededException is thrown from Method
     */
    @PostMapping(path="/enroll")
    public ResponseEntity<EnrollmentResponse> createNewStudentEnrollment(@RequestBody Enrollment enrollment) throws CreditLimitExceededException{
        log.info("StudentsController - Enroll Student");
        //Call Service to Create new Enrollment
        Enrollment e = enrollmentService.saveEnrollment(enrollment);

        //Enrollment Obj saved in DB, send StudentId, SemesterId and ClassName to client
        return new ResponseEntity(new EnrollmentResponse(
                e.getStudent().getStudentId(),
                e.getSemester().getSemId(),
                e.getClassType().getClassName(),
                "Student's Enrollment Record Created. Use Get Request to get Enrollment Details"), HttpStatus.CREATED);
    }

    /**
     * This method accepts StudentId, SemesterId and ClassName and
     * withdraw Enrollment for Semester and Class
     * @Param enrollment
     * @return ResponseEntity<EnrollmentResponse>
     * @throws DataNotFoundException, CreditLimitExceededException is thrown from this method
     */
    @PutMapping(path="/enroll")
    public ResponseEntity<EnrollmentResponse> enrollStudentsEnrollment(@RequestBody Enrollment enrollment) throws DataNotFoundException, CreditLimitExceededException{
        log.info("StudentsController - Enroll Student");

        Enrollment e = enrollmentService.saveEnrollment(enrollment);
        //Enrollment Obj saved in DB, send Student id to client
        return new ResponseEntity(new EnrollmentResponse(
                e.getStudent().getStudentId(),
                e.getSemester().getSemId(),
                e.getClassType().getClassName(),
                "Student's Enrollment is Modified/Enrolled. Use Get Request to get Enrollment Details"), HttpStatus.CREATED);
    }

    /**
     * This method accepts StudentId, SemesterId and ClassName and
     * withdraw Enrollment for Semester and Class
     * @Param enrollment
     * @return ResponseEntity<EnrollmentResponse>
     * @throws DataNotFoundException is thrown from this method
     */
    @PutMapping(path="/withdraw")
    public ResponseEntity<EnrollmentResponse> withdrawStudentsEnrollment(@RequestBody Enrollment enrollment) throws DataNotFoundException {
        log.info("StudentsController - Withdraw Student's Enrollment");
        Enrollment e = enrollmentService.withdrawEnrollment(enrollment);
        //Enrollment Obj saved in DB, send Student id to client
        return new ResponseEntity(new EnrollmentResponse(
                e.getStudent().getStudentId(),
                e.getSemester().getSemId(),
                e.getClassType().getClassName(),
                "Student's Enrollment is withdrawn. Use Get Request to get Enrollment Details"), HttpStatus.OK);
    }
}

