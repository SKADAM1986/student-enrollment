package com.blockone.enrollment.service;

import com.blockone.enrollment.entity.Enrollment;
import com.blockone.enrollment.exceptions.InvalidRequestException;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private final Logger log = LoggerFactory.getLogger(ClassService.class);

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    /**
     * This method accepts className and returns Class Details
     * Once Class information retrieved, it stores same in Cache and reuses if another call is made
     * @Param className
     * @return ClassType
     */
    @Cacheable("classType")
    public ClassType getClassType(String className) {
        log.info("Get Class Type from className - [{}]", className);
        com.blockone.enrollment.entity.ClassType c = classRepository.findByClassName(className);
        if(c != null){
            log.info("Class Object retrieved - [{}]", c);
            return objectMapper.convertToModel(c);
        } else {
            log.error("Class Object not found");
            return null;
        }
    }

    /**
     * This method accepts semesterId and studentId and calls Database to get all enrollments for semesterId And/Or studentId
     * Extracts Classes and returns List of Classes
     * @Param semId
     * @Param studentId
     * @return List<ClassType>
     * @throws InvalidRequestException thrown from this method
     */
    public List<ClassType> getClassesBySemesterStudent(Long semId, Long studentId){
        log.info("ClassService - Get Classes By Semester Id [{}] and Student Id [{}]",semId,studentId );
        List<Enrollment> enrollments;
        if(semId != null && studentId != null) {
            log.info("SemId and StudentId not null, Fetching Data from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrueAndEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(semId,studentId);
        } else if(semId == null && studentId != null) {
            //SemesterId is null, get Enrollments by Student Id
            log.info("StudentId not null, Fetching Data from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(studentId);
        } else {
            //SemesterId is not null, get Enrollments by Semester Id
            log.info("SemId not null, Fetching Data from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrue(semId);
        }
        log.info("Size of enrollment list -> [{}]", + enrollments.size());
        //Retrieved all Enrollments, fetch all Classes from enrollments
        if(CollectionUtils.isEmpty(enrollments)) {
            return new ArrayList<>();
        } else {
            //Extract ClassType from Enrollment List
            return enrollments.stream().map(e -> objectMapper.convertToModel(e.getEnrollmentId().getClassType())).collect(Collectors.toList());
        }
    }


}
