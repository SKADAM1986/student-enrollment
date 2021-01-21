package com.blockone.enrollment.service;

import com.blockone.enrollment.entity.EnrollmentId;
import com.blockone.enrollment.exceptions.CreditLimitExceededException;
import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentRepository;
import com.blockone.enrollment.repository.StudentRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = { "max.allowed.credit=20" })
class EnrollmentServiceTest {

    @InjectMocks
    EnrollmentService enrollmentService;

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Mock
    ClassService classService;

    @Mock
    SemesterService semesterService;

    @Autowired
    @Mock
    ObjectMapper objectMapper;

    List<Enrollment> enrollmentModelList;
    List<com.blockone.enrollment.entity.Enrollment> enrollmentEntityList;
    Enrollment e1Model, e2Model;
    com.blockone.enrollment.entity.Enrollment e1Entity,e2Entity;

    @BeforeEach
    public void initializeEnrollment() {

        Student s1 = new Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        Semester sem1 = new Semester(new Long(1), "Winter-2020", null, null);
        ClassType class1 = new ClassType("2A", 4);
        e1Model = new Enrollment();
        e1Model.setStudent(s1);
        e1Model.setSemester(sem1);
        e1Model.setClassType(class1);

        Student s2 = new Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        Semester sem2 = new Semester(new Long(1), "Winter-2020", null, null);
        ClassType class2 = new ClassType("2A", 4);
        e2Model = new Enrollment();
        e2Model.setStudent(s2);
        e2Model.setSemester(sem2);
        e2Model.setClassType(class2);
        enrollmentModelList = Arrays.asList(e1Model,e2Model);

        ReflectionTestUtils.setField(enrollmentService, "maxAllowedCredit", "20");

        com.blockone.enrollment.entity.Student s1e = new com.blockone.enrollment.entity.Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        com.blockone.enrollment.entity.Semester sem1e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);
        com.blockone.enrollment.entity.ClassType class1e = new com.blockone.enrollment.entity.ClassType("2A", 4);
        e1Entity = new com.blockone.enrollment.entity.Enrollment();
        e1Entity.setEnrollmentId(new EnrollmentId(s1e,sem1e,class1e));

        com.blockone.enrollment.entity.Student s2e = new com.blockone.enrollment.entity.Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        com.blockone.enrollment.entity.Semester sem2e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);
        com.blockone.enrollment.entity.ClassType class2e = new com.blockone.enrollment.entity.ClassType("2A", 4);
        e2Entity = new com.blockone.enrollment.entity.Enrollment();
        e2Entity.setEnrollmentId(new EnrollmentId(s2e,sem2e,class2e));

        enrollmentEntityList = Arrays.asList(e1Entity,e2Entity);

    }

    @Test
    void testCreateNewEnrollment_Positive()
    {
        Semester sem1 = new Semester(new Long(1), "Winter-2020", null, null);

        when(enrollmentRepository.sumCreditPointsForStudent(any(Long.class),any(Long.class))).thenReturn(new Long(10));
        when(classService.getClassType(any(String.class))).thenReturn(new ClassType("2A", 4));
        when(objectMapper.convertToEntity(e1Model)).thenReturn(e1Entity);
        when(objectMapper.convertToModel(e1Entity)).thenReturn(e1Model);

        when(enrollmentRepository.findByEnrollmentId(any(EnrollmentId.class))).thenReturn(null);

        when(semesterService.getSemesterDetails(any(Long.class))).thenReturn(sem1);
        when(enrollmentRepository.save(e1Entity)).thenReturn(e1Entity);

        Assertions.assertEquals(e1Model, enrollmentService.saveEnrollment(e1Model));
    }

    @Test
    void testEnableExistingEnrollment_Positive()
    {
        when(enrollmentRepository.sumCreditPointsForStudent(any(Long.class),any(Long.class))).thenReturn(new Long(10));
        when(classService.getClassType(any(String.class))).thenReturn(new ClassType("2A", 4));
        when(objectMapper.convertToEntity(e1Model)).thenReturn(e1Entity);
        when(enrollmentRepository.findByEnrollmentId(any(EnrollmentId.class))).thenReturn(e1Entity);
        Assertions.assertEquals(e1Model, enrollmentService.saveEnrollment(e1Model));
    }

    @Test
    void testSaveEnrollment_CreditLimitException()
    {
        when(enrollmentRepository.sumCreditPointsForStudent(any(Long.class),any(Long.class))).thenReturn(new Long(19));
        when(classService.getClassType(any(String.class))).thenReturn(new ClassType("2A", 4));
        Assertions.assertThrows(CreditLimitExceededException.class, ()->enrollmentService.saveEnrollment(e1Model));
    }

    @Test
    void testWithdrawEnrollment_Positive()
    {
        when(objectMapper.convertToEntity(e1Model)).thenReturn(e1Entity);
        when(enrollmentRepository.updateActiveIndicator(any(Long.class),any(Long.class),any(String.class), any(Integer.class))).thenReturn(new Integer(1));
        Assertions.assertDoesNotThrow(()-> enrollmentService.withdrawEnrollment(e1Model));
    }

    @Test
    void testWithdrawEnrollment_Exception()
    {
        when(objectMapper.convertToEntity(e1Model)).thenReturn(e1Entity);
        when(enrollmentRepository.updateActiveIndicator(any(Long.class),any(Long.class),any(String.class), any(Integer.class))).thenReturn(new Integer(0));
        Assertions.assertThrows(DataNotFoundException.class, ()-> enrollmentService.withdrawEnrollment(e1Model));
    }

    @Test
    void testGetAllEnrollmentsByClass_Positive()
    {
        when(enrollmentRepository.findAllByEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(any(String.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(e1Entity)).thenReturn(e1Model);
        when(objectMapper.convertToModel(e2Entity)).thenReturn(e2Model);

        List<Enrollment> newEnrollmentList = enrollmentService.getAllEnrollmentsByClass(e1Model.getEnrollmentId().getClassName());

        enrollmentModelList.sort(Comparator.comparing(Enrollment::getEnrollmentId));
        newEnrollmentList.sort(Comparator.comparing(Enrollment::getEnrollmentId));

        assertIterableEquals(enrollmentModelList,newEnrollmentList);

    }

    @Test
    void testGetAllEnrollmentsBySemester_Positive()
    {
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrue(any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(e1Entity)).thenReturn(e1Model);
        when(objectMapper.convertToModel(e2Entity)).thenReturn(e2Model);

        List<Enrollment> newEnrollmentList = enrollmentService.getAllEnrollmentsForSemester(new Long(1));

        enrollmentModelList.sort(Comparator.comparing(Enrollment::getEnrollmentId));
        newEnrollmentList.sort(Comparator.comparing(Enrollment::getEnrollmentId));

        assertIterableEquals(enrollmentModelList,newEnrollmentList);

    }

    @Test
    void testGetAllEnrollmentsByClassAndSemester_Positive()
    {
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndEnrollmentId_ClassType_ClassNameAndActiveIndicatorIsTrue(any(Long.class)
                , any(String.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(e1Entity)).thenReturn(e1Model);
        when(objectMapper.convertToModel(e2Entity)).thenReturn(e2Model);

        List<Enrollment> newEnrollmentList = enrollmentService.getAllEnrollmentsForClassInSemester("2A", new Long(1));

        enrollmentModelList.sort(Comparator.comparing(Enrollment::getEnrollmentId));
        newEnrollmentList.sort(Comparator.comparing(Enrollment::getEnrollmentId));

        assertIterableEquals(enrollmentModelList,newEnrollmentList);

    }
}
