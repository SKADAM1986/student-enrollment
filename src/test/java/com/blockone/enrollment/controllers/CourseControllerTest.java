package com.blockone.enrollment.controllers;

import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.service.ClassService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @InjectMocks
    ClassController classController;

    @Mock
    ClassService classService;
    List<ClassType> list;

    @BeforeEach
    public void initializeEnrollment() {

        list = Arrays.asList(new ClassType("2A", 4), new ClassType("1A", 4));
    }

    @Test
    void testGetAllClassesForSemIdAndStudentIdPositive()
    {
        when(classService.getClassesBySemesterStudent(any(Long.class), any(Long.class))).thenReturn(list);
        List<ClassType> classes = classController.getClassesForStudentsInSemester(new Long(18), new Long(19));
        Assertions.assertEquals(list, classes);
    }

    @Test
    void testGetAllClassesForSemIdAndStudentIdNullSem()
    {
        when(classService.getClassesBySemesterStudent(nullable(Long.class), any(Long.class))).thenReturn(list);
        List<ClassType> classes = classController.getClassesForStudentsInSemester(null, new Long(19));
        Assertions.assertEquals(list, classes);
    }

    @Test
    void testGetAllClassesForSemIdAndStudentIdNullStudent()
    {
        when(classService.getClassesBySemesterStudent(any(Long.class), nullable(Long.class))).thenReturn(list);
        List<ClassType> classes = classController.getClassesForStudentsInSemester(new Long(19), null);
        Assertions.assertEquals(list, classes);
    }

    @Test
    void testGetAllClassesForSemIdAndStudentIdBothNull()
    {
        when(classService.getClassesBySemesterStudent(nullable(Long.class), nullable(Long.class))).thenReturn(Collections.emptyList());
        Assertions.assertEquals(Collections.emptyList(), classController.getClassesForStudentsInSemester(null, null));
    }

    @Test
    void testGetAllClasses()
    {
        when(classService.getAllClasses(nullable(Long.class), nullable(Long.class))).thenReturn(list);
        Assertions.assertEquals(list, classController.getAllClassesEnrolled(null, null));
    }
}
