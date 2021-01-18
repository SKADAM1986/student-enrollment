package com.blockone.enrollment.service;

import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final Logger log = LoggerFactory.getLogger(ClassService.class);
    @Autowired
    StudentRepository studentRepository;

    public Student saveStudent(Student student) {
        log.debug("StudentService.saveStudent() START");
        return studentRepository.save(student);
    }

    public Student getStudentById(Long studentId) {
        log.debug("StudentService.getStudentById() START");
        return studentRepository.findById(studentId).get();
    }

    public List<Student> getAllStudentsByClass(String className){
        log.debug("StudentService.getAllStudentsByClass() START");
        return (List<Student>) studentRepository.findAll();
    }

}
