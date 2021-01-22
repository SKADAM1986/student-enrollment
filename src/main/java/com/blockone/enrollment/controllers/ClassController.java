package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.service.ClassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClassController {

    private final Logger log = LoggerFactory.getLogger(ClassController.class);

    @Autowired
    ClassService classService;

    /**
     * This method accepts semesterId and studentId and calls ClassService getClassesBySemesterStudent method
     * to get all Class details for semesterId And/Or studentId
     * @Param semesterId
     * @Param studentId
     * @return List<ClassType>
     */
    @GetMapping(path={"/semesters/{semesterId}/students/{studentId}/classes",
    "/semesters/students/{studentId}/classes",
            "/semesters/{semesterId}/students/classes"})
    public List<ClassType> getClassesForStudentsInSemester(
            @PathVariable(value = "semesterId", required = false)  Long semesterId,
            @PathVariable(value = "studentId", required = false)  Long studentId) {
        log.info("ClassController - Get Classes By Semester and Student -[{}]{}",semesterId,studentId );
        return classService.getClassesBySemesterStudent(semesterId, studentId);
    }

    /**
     * This method accepts semesterId and studentId and calls ClassService getClassesBySemesterStudent method
     * to get all Class details for semesterId And/Or studentId
     * @Param semesterId
     * @Param studentId
     * @return List<ClassType>
     */
    @GetMapping(path="/classes")
    public List<ClassType> getAllClassesEnrolled(@RequestParam(name = "page", required = false) Long page,
                                                 @RequestParam(name = "size", required = false) Long size) {
        log.info("ClassController - Get Classes with Paging Criteria");
        return classService.getAllClasses(page, size);
    }
}
