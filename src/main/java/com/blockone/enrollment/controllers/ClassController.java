package com.blockone.enrollment.controllers;

import com.blockone.enrollment.service.ClassService;
import com.blockone.enrollment.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClassController {
    private final Logger log = LoggerFactory.getLogger(ClassController.class);

    @Autowired
    EnrollmentService enrollmentService;


    @GetMapping(path={"/semesters/{semester}/students/{studentId}/classes",
    "/semesters/students/{studentId}/classes"})
    public List<String> getClassesForStudentsInSemester(@PathVariable(required=false) String semester,
                                                        @PathVariable String studentId){
        log.debug("ClassController.getClassesForStudentsInSemester() START");


       // log.debug("ClassController.getClassesForStudentsInSemester() START");
        //TODO
        //get ClassTypes based on semesterId and StudentId
        return new ArrayList<String>();
    }
}