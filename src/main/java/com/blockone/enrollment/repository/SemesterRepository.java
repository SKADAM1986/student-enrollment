package com.blockone.enrollment.repository;

import com.blockone.enrollment.entity.Semester;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends CrudRepository<Semester, Long> {
    Semester findBySemId(Long semId);
}

