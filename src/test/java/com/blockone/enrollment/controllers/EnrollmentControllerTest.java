package com.blockone.enrollment.controllers;

import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.*;
import com.blockone.enrollment.service.EnrollmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.*;
import java.time.LocalDate;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EnrollmentControllerTest {

    @InjectMocks
    EnrollmentController enrollmentController;

    @Mock
    EnrollmentService enrollmentService;

    Enrollment e;

    @BeforeEach
    public void initializeEnrollment() {
        Student s = new Student(new Long(1), "George", "Rizzi", LocalDate.now(), LocalDate.now(), "11111111", "USA" );
        Semester sem = new Semester(new Long(1), "Winter-2020", null, null);
        ClassType c = new ClassType("2A", 4);
        e = new Enrollment(s,sem, c, LocalDate.now(), LocalDate.now(), true);
    }

    @Test
    void testCreateEnrollment_Positive()
    {
        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(e);
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.createNewStudentEnrollment(enrollment);
        Assertions.assertEquals(sem.getSemId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testCreateEnrollment_Exception()
    {
        when(enrollmentService.saveEnrollment(Mockito.any(Enrollment.class))).thenThrow(CreditLimitExceededException.class);
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        Assertions.assertThrows(CreditLimitExceededException.class,() -> enrollmentController.createNewStudentEnrollment(enrollment));
    }

    @Test
    void testEnableStudentsEnrollment_Positive()
    {
        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(e);
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.enableStudentsEnrollment(enrollment);
        Assertions.assertEquals(sem.getSemId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testEnableStudentsEnrollment_Exception()
    {
        when(enrollmentService.saveEnrollment(Mockito.any(Enrollment.class))).thenThrow(CreditLimitExceededException.class);
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        Assertions.assertThrows(CreditLimitExceededException.class,() -> enrollmentController.enableStudentsEnrollment(enrollment));
    }

    @Test
    void testWithdrawStudentsEnrollment_Positive()
    {
        doNothing().when(enrollmentService).withdrawEnrollment(any(Enrollment.class));
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.withdrawStudentsEnrollment(enrollment);
        Assertions.assertEquals(sem.getSemId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testWithdrawStudentsEnrollment_Exception()
    {
        doThrow(new DataNotFoundException()).when(enrollmentService).withdrawEnrollment(any(Enrollment.class));
        Enrollment enrollment = new Enrollment();
        Student s = new Student();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        Semester sem = new Semester();
        sem.setSemId(new Long(1));
        enrollment.setSemester(sem);
        ClassType c = new ClassType();
        c.setClassName("2A");
        enrollment.setClassType(c);
        Assertions.assertThrows(DataNotFoundException.class,() -> enrollmentController.withdrawStudentsEnrollment(enrollment));
    }

}
