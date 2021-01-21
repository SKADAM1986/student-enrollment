package com.blockone.enrollment.controllers;

import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.*;
import com.blockone.enrollment.service.StudentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @InjectMocks
    StudentController studentController;

    @Mock
    StudentService studentService;

    List<Student> studentsList;

    @BeforeEach
    public void initializeEnrollment() {
        Student s1 = new Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        Student s2 = new Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        Student s3 = new Student(new Long(3), "Anna", "Ahuja", LocalDate.now(), LocalDate.now(), "333333333", "USA" );
        Student s4 = new Student(new Long(4), "Dona", "Bruce", LocalDate.now(), LocalDate.now(), "444444444", "USA" );
        studentsList = Arrays.asList(s1,s2,s3,s4);
    }

    @Test
    void testCreateStudent_Positive()
    {
        Student s1 = new Student(new Long(111), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        when(studentService.saveStudent(any(Student.class))).thenReturn(s1);
        Student student = new Student();
        student.setStudentId(new Long(111));
        student.setFirstName("Sachin");
        ResponseEntity<StudentResponse> responseEntity = studentController.createStudent(student);
        Assertions.assertEquals(s1.getStudentId(), responseEntity.getBody().getStudentId());
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testModifyStudent_Positive()
    {
        Student s1 = new Student(new Long(123), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        when(studentService.saveStudent(any(Student.class))).thenReturn(s1);
        Student student = new Student();
        student.setStudentId(new Long(123));
        student.setFirstName("Sachin");
        ResponseEntity<StudentResponse> responseEntity = studentController.modifyStudent(student, student.getStudentId());
        Assertions.assertEquals(s1.getStudentId(), responseEntity.getBody().getStudentId());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetStudentById_Positive()
    {
        Student s1 = new Student(new Long(123), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        when(studentService.getStudentById(any(Long.class))).thenReturn(s1);
        Student s = studentController.getStudentById(new Long(1));
        Assertions.assertEquals(s1, s);
    }

    @Test
    void testGetStudentById_Exception()
    {
        doThrow(new DataNotFoundException()).when(studentService).getStudentById(any(Long.class));
        Long l = new Long(123);
        Assertions.assertThrows(DataNotFoundException.class, ()-> studentController.getStudentById(l));
    }

    @Test
    void testGetStudentsByClassName_Positive()
    {
        when(studentService.getAllStudentsByClass(any(String.class))).thenReturn(studentsList);
        List<Student> newList = studentController.getStudentsByClassName("class-name");
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }

    @Test
    void testGetStudentsBySemester_Positive()
    {
        when(studentService.getAllStudentsForSemester(any(Long.class))).thenReturn(studentsList);
        List<Student> newList = studentController.getStudentsBySemester(new Long(1));
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }

    @Test
    void testGetStudentsForClassInSemester_Positive()
    {
        when(studentService.getAllStudentsForClassInSemester(any(String.class),any(Long.class))).thenReturn(studentsList);
        List<Student> newList = studentController.getStudentsForClassInSemester("class-name", new Long(1));
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }
}
