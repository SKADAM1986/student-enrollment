package com.blockone.enrollment.repository;

import com.blockone.enrollment.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends CrudRepository<Enrollment, EnrollmentId> {

    public static final String SUM_CREDITS_QUERY = "SELECT sum(c.credits) from enrollment e, class_info c, semester s " +
            "where e.class_name = c.class_name and e.semester_id = s.sem_id " +
            "and e.student_id = ?1 and s.sem_name =  ?2 and e.active_indicator = 1";

    @Query(value = SUM_CREDITS_QUERY, nativeQuery = true)
    public Long sumCreditPointsForStudent(Long studentId, String semName);

    //List<Enrollment> findEnrollmentByEnrollmentId_ClassName(String className);
    //List<Enrollment> findByEnrollmentId_ClassName(String className);
    //List<Enrollment> findByEnrollmentIdClassName(String className);
    //List<Enrollment> findEnrollmentByEnrollmentId_ClassName(String className);
    //List<Enrollment> findByEnrollmentIdIdClassName(String className);

    List<Enrollment> findAllByEnrollmentId_SemesterSemIdOrEnrollmentId_ClassType_ClassName(Long semId, String className);
    List<Enrollment> findAllByEnrollmentId_SemesterSemIdAndEnrollmentId_ClassType_ClassName(Long semId, String className);
    List<Enrollment> findAllByEnrollmentId_SemesterSemId(Long semesterId);
    List<Enrollment> findAllByEnrollmentId_ClassType_ClassName(Long semesterId);





}

