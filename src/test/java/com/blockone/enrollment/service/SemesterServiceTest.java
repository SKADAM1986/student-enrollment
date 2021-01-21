package com.blockone.enrollment.service;

import com.blockone.enrollment.entity.EnrollmentId;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentRepository;
import com.blockone.enrollment.repository.SemesterRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "max.allowed.credit=20" })
class SemesterServiceTest {

    @InjectMocks
    SemesterService semesterService;

    @Mock
    SemesterRepository semesterRepository;

    List<Semester> semesterModelList;
    List<com.blockone.enrollment.entity.Semester> semesterEntityList;

    Semester sem1,sem2;
    com.blockone.enrollment.entity.Semester sem1e,sem2e;

    @Autowired
    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    public void initializeEnrollment() {

        sem1 = new Semester(new Long(1), "Winter-2020", null, null);
        sem2 = new Semester(new Long(1), "Winter-2020", null, null);

        sem1e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);
        sem2e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);

        semesterModelList = Arrays.asList(sem1,sem2);
        semesterEntityList = Arrays.asList(sem1e, sem1e);


    }

    @Test
    void testCreateSemester_Positive()
    {
        System.out.println(sem1e);
        System.out.println(sem1);
        when(semesterRepository.save(sem1e)).thenReturn(sem1e);
        when(objectMapper.convertToModel(sem1e)).thenReturn(sem1);
        when(objectMapper.convertToEntity(sem1)).thenReturn(sem1e);

        Assertions.assertEquals(sem1, semesterService.createSemester(sem1));
    }

    @Test
    void testGetSemesterDetails_Positive()
    {
        when(semesterRepository.findBySemId(any(Long.class))).thenReturn(sem1e);
        when(objectMapper.convertToModel(sem1e)).thenReturn(sem1);

        Assertions.assertEquals(sem1, semesterService.getSemesterDetails(new Long(1)));
    }

    @Test
    void testGetSemesterDetails_Negative()
    {
        when(semesterRepository.findBySemId(any(Long.class))).thenReturn(null);
        //when(objectMapper.convertToModel(sem1e)).thenReturn(sem1);

        Assertions.assertNull(semesterService.getSemesterDetails(new Long(1)));
    }

}
