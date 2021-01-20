package com.blockone.enrollment.service;

import com.blockone.enrollment.exceptions.DataNotFoundException;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.repository.StudentRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(ClassService.class);
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EnrollmentService enrollmentService;

    /**
     * This method Save Student details in DB
     * @Param student
     * @return Student
     */
    public Student saveStudent(Student student) {
        log.info("StudentService.saveStudent - {}",student.getStudentId());
        return objectMapper.convertToModel(studentRepository.save(objectMapper.convertToEntity(student)));
    }

    /**
     * This Method Get Student Details by ID
     * @Param studentId
     * @return Student
     */
    public Student getStudentById(Long studentId) {
        log.info("StudentService.getStudentById - {}",studentId);
        com.blockone.enrollment.entity.Student student = studentRepository.findById(studentId).
                orElseThrow(DataNotFoundException::new);
        return objectMapper.convertToModel(student);
    }

    /**
     * This method accepts className and calls EnrollmentService getAllEnrollmentsByClass
     * to get all Students for given class
     * @Param className
     * @return List<Student>
     */
    public List<Student> getAllStudentsByClass(String className) {
        log.info("StudentServiceFetch.getAllStudentsByClass - {} ", className );
        return enrollmentService.getAllEnrollmentsByClass(className).stream().map(
                Enrollment::getStudent).collect(Collectors.toList());
    }

    /**
     * This method accepts semesterId and calls EnrollmentService getAllStudentsForSemester
     * to get all Students for given semesterId
     * @Param semesterId
     * @return List<Student>
     */
    public List<Student> getAllStudentsForSemester(Long semesterId) {
        log.info("StudentService.getAllStudentsByClass - {} ", semesterId);
        return enrollmentService.getAllEnrollmentsForSemester(semesterId).stream().map(
                Enrollment::getStudent).collect(Collectors.toList());
    }

    /**
     * This method accepts className/semesterId and calls EnrollmentService getAllStudentsForSemester
     * to get all Students for given semesterId
     * @Param className
     * @Param semId
     * @return List<Student>
     */
    public List<Student> getAllStudentsForClassInSemester(String className,Long semId) {
        log.info("StudentService.getAllStudentsForClassInSemester - className - {}, semesterId - {} ", className, semId);
        return enrollmentService.getAllEnrollmentsForClassInSemester(className, semId).stream().map(
                Enrollment::getStudent).collect(Collectors.toList());
    }
}
