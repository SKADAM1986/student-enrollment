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

    Enrollment enrollment;

    @BeforeEach
    public void initializeEnrollment() {
        Student s = new Student(new Long(1), "George", "Rizzi", LocalDate.now(), LocalDate.now(), "11111111", "USA" );
        Semester sem = new Semester(new Long(1), "Winter-2020", null, null);
        ClassType c = new ClassType("2A", 4);
        enrollment = new Enrollment();
        s.setStudentId(new Long(1));
        enrollment.setStudent(s);
        sem.setSemesterId(new Long(1));
        enrollment.setSemester(sem);
        c.setClassName("2A");
        enrollment.setClassType(c);
    }

    @Test
    void testCreateEnrollment_Positive()
    {
        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(enrollment);

        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.createNewStudentEnrollment(enrollment);

        Assertions.assertEquals(enrollment.getSemester().getSemesterId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testCreateEnrollment_Exception()
    {
        when(enrollmentService.saveEnrollment(Mockito.any(Enrollment.class))).thenThrow(CreditLimitExceededException.class);

        Assertions.assertThrows(CreditLimitExceededException.class,() -> enrollmentController.createNewStudentEnrollment(enrollment));
    }

    @Test
    void testEnableStudentsEnrollment_Positive()
    {
        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(enrollment);
        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.enableStudentsEnrollment(enrollment);
        Assertions.assertEquals(enrollment.getSemester().getSemesterId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(enrollment.getStudent().getStudentId(), responseEntity.getBody().getStudentId());
        Assertions.assertEquals(enrollment.getClassType().getClassName(), responseEntity.getBody().getClassName());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testEnableStudentsEnrollment_Exception()
    {
        when(enrollmentService.saveEnrollment(Mockito.any(Enrollment.class))).thenThrow(CreditLimitExceededException.class);

        Assertions.assertThrows(CreditLimitExceededException.class,() -> enrollmentController.enableStudentsEnrollment(enrollment));
    }

    @Test
    void testWithdrawStudentsEnrollment_Positive()
    {
        doNothing().when(enrollmentService).withdrawEnrollment(any(Enrollment.class));

        ResponseEntity<EnrollmentResponse> responseEntity = enrollmentController.withdrawStudentsEnrollment(enrollment);

        Assertions.assertEquals(enrollment.getSemester().getSemesterId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(enrollment.getStudent().getStudentId(), responseEntity.getBody().getStudentId());
        Assertions.assertEquals(enrollment.getClassType().getClassName(), responseEntity.getBody().getClassName());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testWithdrawStudentsEnrollment_Exception()
    {
        doThrow(new DataNotFoundException()).when(enrollmentService).withdrawEnrollment(any(Enrollment.class));

        Assertions.assertThrows(DataNotFoundException.class,() -> enrollmentController.withdrawStudentsEnrollment(enrollment));
    }

}
