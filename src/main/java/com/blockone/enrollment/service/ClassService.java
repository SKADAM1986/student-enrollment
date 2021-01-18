package com.blockone.enrollment.service;

import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.repository.ClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
    private final Logger log = LoggerFactory.getLogger(ClassService.class);

    @Autowired
    ClassRepository classRepository;

    @Cacheable("classType")
    public ClassType getClassType(String className) {
        log.debug("ClassType.getClassType() START");
        return classRepository.findByClassName(className);
    }
}
