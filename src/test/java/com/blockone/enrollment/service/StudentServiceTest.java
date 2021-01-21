package com.blockone.enrollment.service;

import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.*;
import com.blockone.enrollment.repository.StudentRepository;
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
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @Mock
    EnrollmentService enrollmentService;

    @Autowired
    @Mock
    ObjectMapper objectMapper;

    List<Student> studentsList;
    List<Enrollment> enrollmentList;

    @BeforeEach
    public void initializeEnrollment() {
        Student s1 = new Student(new Long(1), "George", "Fisher", LocalDate.now(), LocalDate.now(), "111112111", "USA" );
        Student s2 = new Student(new Long(2), "Peter", "Walter", LocalDate.now(), LocalDate.now(), "222222222", "USA" );
        Student s3 = new Student(new Long(3), "Anna", "Ahuja", LocalDate.now(), LocalDate.now(), "333333333", "USA" );
        Student s4 = new Student(new Long(4), "Dona", "Bruce", LocalDate.now(), LocalDate.now(), "444444444", "USA" );
        studentsList = Arrays.asList(s1,s2,s3,s4);
        Enrollment e1 = new Enrollment();
        e1.setStudent(s1);
        Enrollment e2 = new Enrollment();
        e2.setStudent(s2);
        Enrollment e3 = new Enrollment();
        e3.setStudent(s3);
        Enrollment e4 = new Enrollment();
        e4.setStudent(s4);
        enrollmentList = Arrays.asList(e1,e2,e3,e4);
    }

    @Test
    void testSaveStudent_Positive()
    {
        com.blockone.enrollment.entity.Student s1Entity = new com.blockone.enrollment.entity.Student(new Long(111), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        Student s2Model = new Student(new Long(123), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);

        when(studentRepository.save(any(com.blockone.enrollment.entity.Student.class))).thenReturn(s1Entity);
        when(objectMapper.convertToModel(s1Entity)).thenReturn(s2Model);
        Student student = new Student();
        student.setStudentId(new Long(111));
        student.setFirstName("Sachin");
        com.blockone.enrollment.entity.Student studentEntity = new com.blockone.enrollment.entity.Student(new Long(111), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        when(objectMapper.convertToEntity(student)).thenReturn(studentEntity);
        Assertions.assertEquals(s2Model, studentService.saveStudent(student));
    }

    @Test
    void testGetStudentById_Positive()
    {
        com.blockone.enrollment.entity.Student s1 = new com.blockone.enrollment.entity.Student(new Long(123), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        Student s2Model = new Student(new Long(123), "Peter", "Kane", LocalDate.now(), LocalDate.now(),null, null);
        when(studentRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(s1));
        when(objectMapper.convertToModel(s1)).thenReturn(s2Model);
        Student s = studentService.getStudentById(new Long(1));
        Assertions.assertEquals(s2Model, s);
    }

    @Test
    void testGetStudentById_Exception()
    {
        doThrow(new DataNotFoundException()).when(studentRepository).findById(any(Long.class));
        Long l = new Long(123);
        Assertions.assertThrows(DataNotFoundException.class, ()-> studentService.getStudentById(l));
    }

    @Test
    void testGetStudentsByClassName_Positive()
    {
        when(enrollmentService.getAllEnrollmentsByClass(any(String.class))).thenReturn(enrollmentList);
        List<Student> newList = studentService.getAllStudentsByClass("class-name");
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }

    @Test
    void testGetStudentsBySemester_Positive()
    {
        when(enrollmentService.getAllEnrollmentsForSemester(any(Long.class))).thenReturn(enrollmentList);
        List<Student> newList = studentService.getAllStudentsForSemester(new Long(1));
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }

    @Test
    void testGetStudentsForClassInSemester_Positive()
    {
        when(enrollmentService.getAllEnrollmentsForClassInSemester(any(String.class),any(Long.class))).thenReturn(enrollmentList);
        List<Student> newList = studentService.getAllStudentsForClassInSemester("class-name", new Long(1));
        newList.sort(Comparator.comparing(Student::getStudentId));
        studentsList.sort(Comparator.comparing(Student::getStudentId));
        assertIterableEquals(newList,studentsList);
    }
}
