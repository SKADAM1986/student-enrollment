package com.blockone.enrollment.service;

import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.repository.SemesterRepository;
import com.blockone.enrollment.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SemesterService {
    private final Logger log = LoggerFactory.getLogger(SemesterService.class);
    @Autowired
    SemesterRepository semesterRepository;

    @Autowired
    ObjectMapper objectMapper;

    public Semester createSemester(Semester semester){
        log.info("Semester.createSemester() - [{}]", semester.getSemesterName());
        //Save Semester Details to DB
        com.blockone.enrollment.entity.Semester s = semesterRepository.save(objectMapper.convertToEntity(semester));
        return objectMapper.convertToModel(s);
    }

    @Cacheable("semester")
    public Semester getSemesterDetails(Long semId) {
        log.info("Semester.getSemesterDetails() - [{}]",semId);
        com.blockone.enrollment.entity.Semester s = semesterRepository.findBySemesterId(semId);
        if(s != null) {
            log.info("Semester Object retrieved - [{}]", s);
            return objectMapper.convertToModel(s);
        } else {
            log.error("Semester Object not found");
            return null;
        }
    }


}