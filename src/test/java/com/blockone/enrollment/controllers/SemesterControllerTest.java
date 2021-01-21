package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.SemesterResponse;
import com.blockone.enrollment.service.ClassService;
import com.blockone.enrollment.service.SemesterService;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SemesterControllerTest {

    @InjectMocks
    SemesterController semesterController;

    @Mock
    SemesterService semesterService;

    @Test
    void testCreateSemester_Positive()
    {
        Semester s1 = new Semester(new Long(1), "Winter-2020", LocalDate.now(), LocalDate.now());
        when(semesterService.createSemester(any(Semester.class))).thenReturn(s1);
        Semester sem = new Semester();
        sem.setSemName("Winter-2020");
        sem.setStartDate(LocalDate.now());
        sem.setEndDate(LocalDate.now());
        ResponseEntity<SemesterResponse> responseEntity = semesterController.createSemester(sem);
        Assertions.assertEquals(s1.getSemId(), responseEntity.getBody().getSemesterId());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

}
