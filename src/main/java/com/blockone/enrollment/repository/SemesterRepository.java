package com.blockone.enrollment.repository;

import com.blockone.enrollment.models.Semester;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {
    public Semester findBySemName(String semName);
}

