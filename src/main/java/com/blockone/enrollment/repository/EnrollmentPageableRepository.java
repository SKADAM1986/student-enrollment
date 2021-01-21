package com.blockone.enrollment.repository;

import com.blockone.enrollment.entity.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, EnrollmentId> {

    String SUM_CREDITS_QUERY = "SELECT IFNULL(sum(c.credits), 0) from enrollment e, class_info c, semester s " +
            "where e.class_name = c.class_name and e.semester_id = s.sem_id " +
            "and e.student_id = ?1 and s.sem_id =  ?2 and e.active_indicator = 1";

    String ENROLLMENT_DATE_QUERY = "SELECT e.enrollment_date from enrollment e " +
            "where e.student_id = ?1 and e.semester_id = ?2 and e.class_name = ?3";

    String UPDATE_ENROLLMENT_QUERY = "UPDATE enrollment e set active_indicator = ?4 where " +
            "e.student_id = ?1 and e.semester_id = ?2 and e.class_name = ?3";

    @Query(value = SUM_CREDITS_QUERY, nativeQuery = true)
    Long sumCreditPointsForStudent(Long studentId, Long semId);

    @Query(value = ENROLLMENT_DATE_QUERY, nativeQuery = true)
    LocalDate getEnrollmentDate(Long studentId, Long semId, String classType);

    @Modifying
    @Query(value = UPDATE_ENROLLMENT_QUERY, nativeQuery = true)
    int updateActiveIndicator(Long studentId, Long semId, String className, int active);

    Enrollment findByEnrollmentId(EnrollmentId enrollmentId);

    List<Enrollment> findAllByEnrollmentId_SemesterSemIdAndEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(Long semId, String className);

    List<Enrollment> findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrueAndEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(Long semId, Long studentId);

    List<Enrollment> findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(Long studentId);

    List<Enrollment> findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrue(Long semId);

    List<Enrollment> findAllByEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(String className);


    List<Enrollment> findAllByEnrollmentId_SemesterSemId(Long semesterId);

    List<Enrollment> findAllByEnrollmentId_ClassType_ClassName(String className);





}

