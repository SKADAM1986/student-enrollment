package com.blockone.enrollment.service;

import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.repository.SemesterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {
    private final Logger log = LoggerFactory.getLogger(ClassService.class);
    @Autowired
    SemesterRepository semesterRepository;

    public Semester createSemester(Semester semester){
        //Add record in Semester Table and return semester id
        log.debug("Semester.createSemester() START");
        semesterRepository.save(semester);
        return semester;
    }

    @Cacheable("semester")
    public Semester getSemesterDetails(String semName) {
        log.debug("Semester.getSemesterDetails() START");
        return semesterRepository.findBySemName(semName);
    }
}
