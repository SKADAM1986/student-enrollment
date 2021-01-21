package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.SemesterResponse;
import com.blockone.enrollment.service.SemesterService;
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
public class SemesterController {

    private final Logger log = LoggerFactory.getLogger(SemesterController.class);
    
    @Autowired
    SemesterService semesterService;

    /**
     * This method Creates new Semester and returns Semester Id to Client
     * @Param semester
     * @return ResponseEntity<Semester>
     */
    @PostMapping(path="/semesters")
    public ResponseEntity<SemesterResponse> createSemester(@RequestBody Semester semester) {
        log.info("SemesterController.createSemester()");
        //Call service to create Semester and return updated Semester info
        //insert data in semester table
        Semester s = semesterService.createSemester(semester);
        //Semester Obj saved in DB, send Semester id to client
        return new ResponseEntity<SemesterResponse>(new SemesterResponse(s.getSemId(),
                "New Semester is Created. Use Get Request to get Semester Details"), HttpStatus.CREATED);
    }


    
}
