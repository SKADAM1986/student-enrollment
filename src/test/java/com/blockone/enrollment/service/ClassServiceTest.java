package com.blockone.enrollment.service;

import com.blockone.enrollment.entity.EnrollmentId;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.repository.ClassRepository;
import com.blockone.enrollment.repository.EnrollmentPageableRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @Mock
    EnrollmentPageableRepository enrollmentPageableRepository;

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
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemesterIdAndActiveIndicatorIsTrueAndEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(any(Long.class), any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(new Long(1), new Long(1));

        List<ClassType> classes = classModelList.stream().distinct().collect(Collectors.toList());
        classes.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classes,newClassList);
    }

    @Test
    void testGetClassesBySemesterStudent_Positive_StudentId()
    {
        when(enrollmentRepository.findAllByEnrollmentId_StudentStudentIdAndActiveIndicatorIsTrue(any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(null, new Long(1));

        List<ClassType> classes = classModelList.stream().distinct().collect(Collectors.toList());
        classes.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classes,newClassList);
    }

    @Test
    void testGetClassesBySemesterStudent_Positive_SemesterId()
    {
        when(enrollmentRepository.findAllByEnrollmentId_SemesterSemesterIdAndActiveIndicatorIsTrue(any(Long.class))).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getClassesBySemesterStudent(new Long(1), null);

        List<ClassType> classes = classModelList.stream().distinct().collect(Collectors.toList());
        classes.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classes,newClassList);
    }

    @Test
    void testGetAllClasses_Positive_All()
    {
        when(enrollmentRepository.findAll()).thenReturn(enrollmentEntityList);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getAllClasses(null, null);

        List<ClassType> classes = classModelList.stream().distinct().collect(Collectors.toList());
        classes.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classes, newClassList);
    }

    @Test
    void testGetAllClasses_Positive_Paging()
    {
        Page enrollmentPage = new PageImpl(enrollmentEntityList);
        when(enrollmentPageableRepository.findAll(any(Pageable.class))).thenReturn(enrollmentPage);
        when(objectMapper.convertToModel(class1e)).thenReturn(class1);
        when(objectMapper.convertToModel(class2e)).thenReturn(class2);

        List<ClassType> newClassList = classService.getAllClasses(new Long(0), new Long(2));

        List<ClassType> classes = classModelList.stream().distinct().collect(Collectors.toList());
        classes.sort(Comparator.comparing(ClassType::getClassName));
        newClassList.sort(Comparator.comparing(ClassType::getClassName));

        assertIterableEquals(classes,newClassList);
    }

}
