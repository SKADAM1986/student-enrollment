package com.blockone.enrollment.service;

import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentRepository;
import com.blockone.enrollment.repository.SemesterRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:enrollment.properties")
public class EnrollmentService {

    private final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    @Value("${max.allowed.credit}")
    private String maxAllowedCredit;

    @Autowired
    ObjectMapper objectMapper;
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
    @Autowired
    StudentService studentService;

    /**
     * This method save Enrollment details, If Enrollment is first time, it will store entire object in DB
     * IF Re-enrollment request, it will update active indicator
     * @Param enrollmentModel
     * @return Enrollment
     */
    @Transactional
    public Enrollment saveEnrollment(Enrollment enrollmentModel) {
        log.info("EnrollmentService.saveEnrollment()");
        log.info("Get Total Credit for all Enrolled Classes [{}]", enrollmentModel.getEnrollmentId());
        log.info("Student Id -[{}]Sem Id - {}", enrollmentModel.getStudent().getStudentId(), enrollmentModel.getSemester().getSemesterId());

        //Before Saving Enrollment, get sum of Credits for all enrolled classes for this semester
        Long totalCredits = enrollmentRepository.sumCreditPointsForStudent(
                enrollmentModel.getStudent().getStudentId()
                , enrollmentModel.getSemester().getSemesterId());
        log.info("Enrolled Classes total Credit ->[{}]", totalCredits);

        //Get Credit points for current requested class.
        ClassType c = classService.getClassType(enrollmentModel.getClassType().getClassName());

        log.info("Requested Class has[{}]Credits", c.getCreditPoints());
        if(totalCredits.intValue() + c.getCreditPoints() > Integer.valueOf(maxAllowedCredit)) {
            log.error("Total Credit[{}]is more than limit. Throw CreditLimitExceededException", totalCredits);
            String err = "Max Credit Limit per semester is [{" + maxAllowedCredit + "}]. You have already enrolled for [{"
                    + totalCredits + "}] in this Semester. Class you are enrolling for has [{" + c.getCreditPoints()
                    + "}] Credits. Please try to enroll to another semester.";
            throw new CreditLimitExceededException(err);
        } else {
            log.info("Total Credit [{}] is within limit. Proceed for Enrollment", totalCredits);
            com.blockone.enrollment.entity.Enrollment enrollmentEntity = objectMapper.convertToEntity(enrollmentModel);
            //Get Enrollment data from DB based on Student Id, Semester Id and Class Name
            com.blockone.enrollment.entity.Enrollment e = enrollmentRepository.findByEnrollmentId(
                    enrollmentEntity.getEnrollmentId());

            //If Enrollment is already exist, just update active indicator to true
            //If Enrollment does not exist, just update active indicator to false
            if(e != null) {
                int returnVal = enrollmentRepository.updateActiveIndicator(e.getEnrollmentId().getStudent().getStudentId(),
                        e.getEnrollmentId().getSemester().getSemesterId(),
                        e.getEnrollmentId().getClassType().getClassName(), 1);
                log.info("Return Value After Update Query - [{}]" , returnVal);
            } else {
                //Set Active Indicator to true when it is created
                enrollmentEntity.setActiveIndicator(true);
                //Set Default date for Enrollment Date and LastUpdateDate
                enrollmentEntity.setEnrollmentDate(LocalDate.now());
                enrollmentEntity.setLastUpdateDate(LocalDate.now());
                //Populate Semester Name to add to Enrollment table
                populateRelatedData(enrollmentEntity);
                //Save Enrollment Entity to DB
                enrollmentModel = objectMapper.convertToModel(enrollmentRepository.save(enrollmentEntity));
            }
            return enrollmentModel;
        }
    }

    /**
     * This method is used to Withdraw Enrollment, it just update ActiveIndicator value to false
     * @Param enrollmentModel
     * @throws DataNotFoundException thrown from this method
     */
    @Transactional
    public void withdrawEnrollment(Enrollment enrollmentModel) {
        log.info("EnrollmentService.withdrawEnrollment()");
        com.blockone.enrollment.entity.Enrollment enrollmentEntity = objectMapper.convertToEntity(enrollmentModel);
        log.info("Calling enrollmentRepository.updateActiveIndicator(...)");
        //Set active_indicator to 0 in enrollments table for this Student, semester and class
        int returnVal = enrollmentRepository.updateActiveIndicator(
                enrollmentEntity.getEnrollmentId().getStudent().getStudentId(),
                enrollmentEntity.getEnrollmentId().getSemester().getSemesterId(),
                enrollmentEntity.getEnrollmentId().getClassType().getClassName(), 0
                );
        log.info("Return Value After Update Query - [{}]",returnVal);
        //No data found based on requested criteria.
        if(returnVal == 0) {
            throw new DataNotFoundException("No Record found in Database. Please verify inputs");
        }
    }

    /**
     * Get Semester Details from semester Table
     * Get Student Details from students Table
     * Get Class Details Details from class_info Table
     * @Param enrollmentEntity
     */
    public void populateRelatedData(com.blockone.enrollment.entity.Enrollment enrollmentEntity){
        log.info("EnrollmentService.populateRelatedData - Fetch Semester Details from DB");
        //Fetch Semester Details based on Semester id
        enrollmentEntity.getEnrollmentId().setSemester(
                objectMapper.convertToEntity(
                        semesterService.getSemesterDetails(enrollmentEntity.getEnrollmentId().getSemester().getSemesterId())));
        //As of now Student and Class information not needed
    }

    /**
     * This method accepts className and calls getAllEnrollments to get all enrollments for given class
     * @Param className
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsByClass(String className) {
        log.info("Fetch student by class -[{}]", className );
        //Call Generic method with null sem id
        return getAllEnrollments(className, null);
    }
    /**
     * This method accepts semesterId and calls getAllEnrollments to get all enrollments for given semesterId
     * @Param semesterId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsForSemester(Long semesterId) {
        log.info("EnrollmentService.getAllStudentsEnrolledForSemester - [{}]",semesterId);
        //Call Generic method with null class name
        return getAllEnrollments(null, semesterId);
    }
    /**
     * This method accepts semesterId and calls getAllEnrollments to get all enrollments for given class and semesterId
     * @Param className
     * @Param semId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsForClassInSemester(String className,Long semesterId) {
        log.info("EnrollmentService.getAllStudentsEnrolledForClassInSemester - SemesterId [{}] , className [{}]",semesterId, className);
        //Both values are present
        return getAllEnrollments(className, semesterId);
    }

    /**
     * This method accepts semesterId and calls database to get all enrollments for given class and/or semesterId
     * @Param className
     * @Param semesterId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollments(String className, Long semesterId){
        log.info("EnrollmentService.getAllEnrollments");
        List<com.blockone.enrollment.entity.Enrollment> enrollments;
        //Check which parameter is received,
        //if className AND semesterId Received, Call based on AND condition
        //if className only OR semesterId only Received, Call based on OR condition
        //We need to fetch only Active enrollments
        if(className != null && semesterId != null) {
            log.info("className and  SemesterId not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemesterIdAndEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(
                    semesterId, className);
        } else if(className == null && semesterId != null) {
            log.info("className is null and semesterId is not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemesterIdAndActiveIndicatorIsTrue(semesterId);
        } else {
            log.info("className is not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(className);
        }
        //Once Data retrieved, map to Enrollment Model Object to further processing
        return enrollments.stream().map( obj -> objectMapper.convertToModel(obj) ).collect(Collectors.toList());
    }
}
