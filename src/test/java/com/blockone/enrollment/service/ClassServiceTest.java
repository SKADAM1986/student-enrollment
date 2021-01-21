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
import org.springframework.test.util.ReflectionTestUtils;

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
class ClassServiceTest {

    @InjectMocks
    ClassService classService;

    @Mock
    ClassRepository classRepository;

    @Mock
    EnrollmentRepository enrollmentRepository;

    @Autowired
    @Mock
    ObjectMapper objectMapper;

    List<Enrollment> enrollmentModelList;
    List<com.blockone.enrollment.entity.Enrollment> enrollmentEntityList;
    Enrollment e1Model, e2Model;
    List<ClassType> classModelList;
    List<com.blockone.enrollment.entity.ClassType> classEntityList;
    ClassType class1,class2;
    com.blockone.enrollment.entity.ClassType class1e, class2e;
    com.blockone.enrollment.entity.Enrollment e1Entity,e2Entity;
    @BeforeEach
    public void initializeEnrollment() {

        Student s1 = new Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        Semester sem1 = new Semester(new Long(1), "Winter-2020", null, null);
        class1 = new ClassType("2A", 4);
        e1Model = new Enrollment();
        e1Model.setStudent(s1);
        e1Model.setSemester(sem1);
        e1Model.setClassType(class1);


        Student s2 = new Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        Semester sem2 = new Semester(new Long(1), "Winter-2020", null, null);
        class2 = new ClassType("2A", 4);
        e2Model = new Enrollment();
        e2Model.setStudent(s2);
        e2Model.setSemester(sem2);
        e2Model.setClassType(class2);
        enrollmentModelList = Arrays.asList(e1Model,e2Model);
        classModelList = Arrays.asList(class1, class2);

        com.blockone.enrollment.entity.Student s1e = new com.blockone.enrollment.entity.Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        com.blockone.enrollment.entity.Semester sem1e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);
        class1e = new com.blockone.enrollment.entity.ClassType("2A", 4);
        e1Entity = new com.blockone.enrollment.entity.Enrollment();
        e1Entity.setEnrollmentId(new EnrollmentId(s1e,sem1e,class1e));

        com.blockone.enrollment.entity.Student s2e = new com.blockone.enrollment.entity.Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        com.blockone.enrollment.entity.Semester sem2e = new com.blockone.enrollment.entity.Semester(new Long(1), "Winter-2020", null, null);
        class2e = new com.blockone.enrollment.entity.ClassType("2A", 4);
        e2Entity = new com.blockone.enrollment.entity.Enrollment();
        e2Entity.setEnrollmentId(new EnrollmentId(s2e,sem2e,class2e));
        enrollmentEntityList = Arrays.asList(e1Entity,e2Entity);
        classEntityList = Arrays.asList(class1e, class2e);


    }

    @Test
    void testGetClassType_Positive()
    {
        when(classRepository.findByClassName(any(String.class))).thenReturn(class1e);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);

        Assertions.assertEquals(class1, classService.getClassType("2A"));
    }

    @Test
    void testGetClassType_Negative()
    {
        when(classRepository.findByClassName(any(String.class))).thenReturn(null);
        Assertions.assertNull(classService.getClassType("2A"));
    }


    @Test
    void testGetClassesBySemesterStudent_Positive_BothPresent()
    {
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrueAndEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(any(Long.class), any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(new Long(1), new Long(1));

        classModelList.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classModelList,newClassList);
    }

    @Test
    void testGetClassesBySemesterStudent_Positive_StudentId()
    {
        when(enrollmentRepository.findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(null, new Long(1));

        classModelList.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classModelList,newClassList);
    }

    @Test
    void testGetClassesBySemesterStudent_Positive_SemesterId()
    {
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemIdAndActiveIndicatorIsTrue(any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(new Long(1), null);

        classModelList.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classModelList,newClassList);
    }
/*
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

    }*/
}
