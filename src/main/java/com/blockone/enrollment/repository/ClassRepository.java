package com.blockone.enrollment.repository;

import com.blockone.enrollment.entity.ClassType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends CrudRepository<ClassType, String> {

    ClassType findByClassName(String className);

}

