package com.blockone.enrollment.repository;

import com.blockone.enrollment.entity.Enrollment;
import com.blockone.enrollment.entity.EnrollmentId;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnrollmentPageableRepository extends PagingAndSortingRepository<Enrollment, EnrollmentId> {

}

