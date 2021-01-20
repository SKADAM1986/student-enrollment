package com.blockone.enrollment.service;

import com.blockone.enrollment.entity.Enrollment;
import com.blockone.enrollment.exceptions.CreditLimitExceededException;
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
        log.info("Get Class Type from className - {}", className);
        return objectMapper.convertToModel(classRepository.findByClassName(className));
    }

    /**
     * This method accepts semesterId and studentId and calls Database to get all enrollments for semesterId And/Or studentId
     * Extracts Classes and returns List of Classes
     * @Param semId
     * @Param studentId
     * @return List<ClassType>
     * @throws InvalidRequestException thrown from this method
     */
    public List<ClassType> getClassesBySemesterStudent(Long semId, Long studentId) throws InvalidRequestException {
        log.info("ClassService - Get Classes By Semester and Student - {} {}",semId,studentId );
        List<Enrollment> enrollments;
        if(semId != null && studentId != null) {
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrueAndEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(semId,studentId);
        } else if(semId == null && studentId != null) {
            //SemesterId is null, get Enrollments by Student Id
            enrollments = enrollmentRepository.findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(studentId);
        } else if(studentId == null && semId != null) {
            //SemesterId is null, get Enrollments by Student Id
            enrollments = enrollmentRepository.findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(semId);
        } else {
            throw new InvalidRequestException("Both Student Id and SemesterId is null. No matching criteria found");
        }

        //Retrieved all Enrollments, fetch all Classes from enrollments
        if(enrollments == null || enrollments.isEmpty()) {
            return new ArrayList<>();
        } else {
            return enrollments.stream().map(e -> objectMapper.convertToModel(e.getEnrollmentId().getClassType())).collect(Collectors.toList());
        }
    }


}
