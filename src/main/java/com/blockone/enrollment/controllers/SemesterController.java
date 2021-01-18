package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.service.ClassService;
import com.blockone.enrollment.service.SemesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/hi")
    public String helloWorld(){

        return "HelloWorld in Semester";
    }

    @PostMapping(path="/semesters")
    public Semester createSemester(@RequestBody Semester semester) {
        log.debug("SemesterController.createSemester() START");
        //Call service to create Student and return updated Student info
        //insert data in semester table
        return semesterService.createSemester(semester);
    }

}
