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
     * @throws CreditLimitExceededException, NoRecordFoundException thrown from this method
     */
    @Transactional
    public Enrollment saveEnrollment(Enrollment enrollmentModel) throws CreditLimitExceededException, DataNotFoundException {
        log.info("EnrollmentService.saveEnrollment()");
        log.info("Get Total Credit for all Enrolled Classes "+ enrollmentModel);
        log.info("Student Id - {} Sem Id - {}", enrollmentModel.getStudent().getStudentId(), enrollmentModel.getSemester().getSemId());
        Long totalCredits = enrollmentRepository.sumCreditPointsForStudent(
                enrollmentModel.getStudent().getStudentId()
                , enrollmentModel.getSemester().getSemId());
        log.info("Enrolled Classes total Credit -> "+ totalCredits);

        ClassType c = classService.getClassType(enrollmentModel.getClassType().getClassName());
        log.info("Requested Class has {} Credits", c.getCreditPoints());

        if(totalCredits.intValue() + c.getCreditPoints() > Integer.valueOf(maxAllowedCredit)) {
            log.error("Total Credit {} is more than limit. Throw CreditLimitExceededException", totalCredits);
            String err = "Max Credit Limit per semester is {" + maxAllowedCredit + "}. You have already enrolled for {"
                    + totalCredits + "} in this Semester. Class you are enrolling for has {" + c.getCreditPoints() + "} Credits. Please try to enroll to another semester.";
            throw new CreditLimitExceededException(err);
        } else {
            log.info("Total Credit {} is within limit. Proceed for Enrollment", totalCredits);
            com.blockone.enrollment.entity.Enrollment enrollmentEntity = objectMapper.convertToEntity(enrollmentModel);
            //Get Enrollment data from DB based on Student Id, Semester Id and Class Name
            com.blockone.enrollment.entity.Enrollment e = enrollmentRepository.findByEnrollmentId(
                    enrollmentEntity.getEnrollmentId());

            //If Enrollment is already exist, just update active indicator to true
            //If Enrollment does not exist, just update active indicator to false
            if(e != null) {
                int returnVal = enrollmentRepository.updateActiveIndicator(e.getEnrollmentId().getStudent().getStudentId(),
                        e.getEnrollmentId().getSemester().getSemId(),
                        e.getEnrollmentId().getClassType().getClassName(), 1);
                log.info("Return Value After Update Query - "+returnVal);
                if(returnVal == 0) {
                    throw new DataNotFoundException("No Record found in Database. Please verify inputs");
                }
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
     * @return Enrollment
     * @throws DataNotFoundException thrown from this method
     */
    @Transactional
    public Enrollment withdrawEnrollment(Enrollment enrollmentModel) throws DataNotFoundException {
        log.info("EnrollmentService.withdrawEnrollment()");
        com.blockone.enrollment.entity.Enrollment enrollmentEntity = objectMapper.convertToEntity(enrollmentModel);
        log.info("Calling enrollmentRepository.updateActiveIndicator(...)");
        int returnVal = enrollmentRepository.updateActiveIndicator(
                enrollmentEntity.getEnrollmentId().getStudent().getStudentId(),
                enrollmentEntity.getEnrollmentId().getSemester().getSemId(),
                enrollmentEntity.getEnrollmentId().getClassType().getClassName(), 0
                );
        log.info("Return Value After Update Query - "+returnVal);
        if(returnVal == 0) {
            throw new DataNotFoundException("No Record found in Database. Please verify inputs");
        }
        return enrollmentModel;
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
                        semesterService.getSemesterDetails(enrollmentEntity.getEnrollmentId().getSemester().getSemId())));
        //As of now, code is commented for Class Details and Student Information.
        // But can be used if need to send data to client
        /*Fetch Class Details based on Class Name
        enrollmentEntity.getEnrollmentId().setClassType(
                objectMapper.convertToEntity(
                        classService.getClassType(enrollmentEntity.getEnrollmentId().getClassType().getClassName())));

        //Fetch Student Details based on Student id
        enrollmentEntity.getEnrollmentId().setStudent(
                objectMapper.convertToEntity(
                        studentService.getStudentDetails(enrollmentEntity.getEnrollmentId().getStudent().getStudentId())));

        //Populate enrollment date from DB if student requested for withdrawal
        if(enrollmentEntity.getEnrollmentDate() == null) {
            log.info("enrollmentEntity.getEnrollmentDate() is null, fetching from database");
            enrollmentEntity.setEnrollmentDate(enrollmentRepository.getEnrollmentDate(
                    enrollmentEntity.getEnrollmentId().getStudent().getStudentId(),
                    enrollmentEntity.getEnrollmentId().getSemester().getSemId(),
                    enrollmentEntity.getEnrollmentId().getClassType().getClassName()
            ));
            log.info("about to finish enrollmentEntity.getEnrollmentDate() -> " + enrollmentEntity.getEnrollmentDate());
        }*/
    }

    /**
     * This method accepts className and calls getAllEnrollments to get all enrollments for given class
     * @Param className
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsByClass(String className) {
        log.info("Fetch student by class - {} ", className );
        return getAllEnrollments(className, null);
    }
    /**
     * This method accepts semesterId and calls getAllEnrollments to get all enrollments for given semesterId
     * @Param semesterId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsForSemester(Long semesterId) {
        log.info("EnrollmentService.getAllStudentsEnrolledForSemester() START");
        return getAllEnrollments(null, semesterId);
    }
    /**
     * This method accepts semesterId and calls getAllEnrollments to get all enrollments for given class and semesterId
     * @Param className
     * @Param semId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollmentsForClassInSemester(String className,Long semId) {
        log.info("EnrollmentService.getAllStudentsEnrolledForClassInSemester() START");
        return getAllEnrollments(className, semId);
    }

    /**
     * This method accepts semesterId and calls database to get all enrollments for given class and/or semesterId
     * @Param className
     * @Param semesterId
     * @return List<Enrollment>
     */
    public List<Enrollment> getAllEnrollments(String className, Long semesterId){
        log.info("EnrollmentService.getAllEnrollments() START");
        List<com.blockone.enrollment.entity.Enrollment> enrollments;
        //Check which parameter is received,
        //if className AND semesterId Received, Call based on AND condition
        //if className only OR semesterId only Received, Call based on OR condition
        //We need to fetch only Active enrollments
        if(className != null && semesterId != null) {
            log.info("className and  SemesterId not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(
                    semesterId, className);
        } else if(className == null && semesterId != null) {
            log.info("semesterId is not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrue(semesterId);
        } else if(semesterId == null && className != null) {
            log.info("className is not null. Fetching records from DB");
            enrollments = enrollmentRepository.findAllByEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(className);
        } else {
            throw new DataNotFoundException("No Record found in Database. Please verify inputs");
        }

        //Once Data retrieved, map to Enrollment Model Object to further processing
        return enrollments.stream().map( obj -> objectMapper.convertToModel(obj) ).collect(Collectors.toList());
    }
}
