package com.blockone.enrollment.service;

import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.models.*;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentRepository;
import com.blockone.enrollment.repository.SemesterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:enrollment.properties")
public class EnrollmentService {

    private final Logger log = LoggerFactory.getLogger(EnrollmentService.class);
    @Value("${max.allowed.credit}")
    private Long maxAllowedCredit;

    @Autowired
    EnrollmentRepository enrollmentRepository;
    @Autowired
    SemesterRepository semesterRepository;
    @Autowired
    ClassRepository classRepository;
    @Autowired
    ClassService classService;
    @Autowired
    SemesterService semesterService;

    public Enrollment saveEnrollment(Enrollment enrollment) throws CreditLimitExceededException {
        log.debug("EnrollmentService.saveEnrollment() START");
        log.debug("get Credits - "+ enrollment.getEnrollmentId().getStudent().getId() +
                " & " + enrollment.getEnrollmentId().getSemester().getSemName());
        Long totalCredits = enrollmentRepository.sumCreditPointsForStudent(
                enrollment.getEnrollmentId().getStudent().getId()
                , enrollment.getEnrollmentId().getSemester().getSemName());

        if(totalCredits.compareTo(maxAllowedCredit) != 0) {
            log.error("Enrolled Classes total Credit is more than limit "+ totalCredits);
            throw new CreditLimitExceededException("Credit Limit Exceeded (max 20) per Semester. Please try to enroll to another semester.");
        } else {
            log.info("Credit is within limit "+ totalCredits);
            enrollment.getEnrollmentId().setSemester(
                    semesterService.getSemesterDetails(enrollment.getEnrollmentId().getSemester().getSemName()));
            enrollment.getEnrollmentId().setClassType(
                    classService.getClassType(enrollment.getEnrollmentId().getClassType().getClassName()));
            return enrollmentRepository.save(enrollment);
        }
    }

    public List<Enrollment> getAllStudentsByClass1111(String className){
        log.debug("EnrollmentService.getAllStudentsByClass() START");
        System.out.println("222222222222222");
        ClassType c = new ClassType();
        c.setClassName(className);
        Semester s = new Semester();
        s.setSemId(new Long(2));

        //  Working code
        //  enrollmentRepository.findById(new EnrollmentId(new Long(8), s, c)).ifPresent(System.out::println);
        System.out.println("333333333333333");
        //return (List<Enrollment>) enrollmentRepository.findAll();
        //return (List<Enrollment>) enrollmentRepository.findAllByEnrollmentIdSemesterSemId(s.getSemId());
        return (List<Enrollment>) enrollmentRepository.findAllByEnrollmentId_SemesterSemIdOrEnrollmentId_ClassType_ClassName(s.getSemId(), "");
        //return null;
    }
    public List<Enrollment> getAllStudentsByClass(String className) {
        log.debug("Fetch student by class - {} ", className );
       /* ClassType c = new ClassType();
        c.setClassName(className);*/
        return getAllStudents(className, null);
    }

    public List<Enrollment> getAllStudentsForSemester(Long semesterId) {
        log.debug("EnrollmentService.getAllStudentsEnrolledForSemester() START");
/*        Semester s = new Semester();
        s.setSemId(semesterId);//Pass Sem Id*/
        return getAllStudents(null, semesterId);
    }

    public List<Enrollment> getAllStudentsEnrolledForClassInSemester(String className,Long semId) {
        log.debug("EnrollmentService.getAllStudentsEnrolledForClassInSemester() START");
        return getAllStudents(className, semId);
    }

    public List<Enrollment> getAllStudents(String className, Long semesterId){
        log.debug("EnrollmentService.getAllStudents() START");
        /*     ClassType c = new ClassType();
        c.setClassName(className);
        Semester s = new Semester();
        s.setSemId(new Long(2));*/

        //  Working code
        //  enrollmentRepository.findById(new EnrollmentId(new Long(8), s, c)).ifPresent(System.out::println);
        //return (List<Enrollment>) enrollmentRepository.findAll();
        //return (List<Enrollment>) enrollmentRepository.findAllByEnrollmentIdSemesterSemId(s.getSemId());
        //if()
        if(className != null && semesterId != null) {
            return (List<Enrollment>) enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndEnrollmentId_ClassType_ClassName(
                    semesterId, className);
        } else {
            return (List<Enrollment>) enrollmentRepository.findAllByEnrollmentId_SemesterSemIdOrEnrollmentId_ClassType_ClassName(
                    semesterId, className);
        }



        //return null;
    }

}
