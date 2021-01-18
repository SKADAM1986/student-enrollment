package com.blockone.enrollment.repository;

import com.blockone.enrollment.models.ClassType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends CrudRepository<ClassType, String> {

    public ClassType findByClassName(String className);

}

