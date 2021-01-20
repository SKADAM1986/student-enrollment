package com.blockone.enrollment.util;

import com.blockone.enrollment.entity.EnrollmentId;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.Student;
import com.blockone.enrollment.service.EnrollmentService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {

    @Autowired
    ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(ObjectMapper.class);

    public com.blockone.enrollment.entity.Enrollment convertToEntity(Enrollment enrollmentModel) {
        log.info("enrollmentModel "+enrollmentModel);
        com.blockone.enrollment.entity.Enrollment enrollmentEntity = modelMapper.map(enrollmentModel, com.blockone.enrollment.entity.Enrollment.class);
        enrollmentEntity.setEnrollmentId(new EnrollmentId(
                this.convertToEntity(enrollmentModel.getStudent()),
                this.convertToEntity(enrollmentModel.getSemester()),
                this.convertToEntity(enrollmentModel.getClassType()))
        );
        log.info("enrollmentEntity "+enrollmentEntity);
        return enrollmentEntity;
    }

    public Enrollment convertToModel(com.blockone.enrollment.entity.Enrollment enrollmentEntity) {
        log.info("enrollmentEntity "+enrollmentEntity);
        Enrollment enrollmentModel = modelMapper.map(enrollmentEntity, Enrollment.class);
        enrollmentModel.setStudent(this.convertToModel(enrollmentEntity.getEnrollmentId().getStudent()));
        enrollmentModel.setSemester(this.convertToModel(enrollmentEntity.getEnrollmentId().getSemester()));
        enrollmentModel.setClassType(this.convertToModel(enrollmentEntity.getEnrollmentId().getClassType()));
        log.info("enrollmentModel "+enrollmentModel);
        return enrollmentModel;
    }

    public com.blockone.enrollment.entity.Student convertToEntity(Student studentModel) {
        log.info("studentModel "+studentModel);
        com.blockone.enrollment.entity.Student studentEntity = modelMapper.map(studentModel, com.blockone.enrollment.entity.Student.class);
        log.info("studentEntity "+studentEntity);
        return studentEntity;
    }

    public Student convertToModel(com.blockone.enrollment.entity.Student studentEntity) {
        log.info("studentEntity "+studentEntity);
        Student studentModel = modelMapper.map(studentEntity, Student.class);
        log.info("studentModel "+studentModel);
        return studentModel;
    }

    public com.blockone.enrollment.entity.Semester convertToEntity(Semester semesterModel) {
        com.blockone.enrollment.entity.Semester semesterEntity = modelMapper.map(semesterModel, com.blockone.enrollment.entity.Semester.class);
        return semesterEntity;
    }

    public Semester convertToModel(com.blockone.enrollment.entity.Semester semesterEntity) {
        Semester semesterModel = modelMapper.map(semesterEntity, Semester.class);
        return semesterModel;
    }

    public com.blockone.enrollment.entity.ClassType convertToEntity(ClassType classTypeModel) {
        com.blockone.enrollment.entity.ClassType classTypeEntity = modelMapper.map(classTypeModel, com.blockone.enrollment.entity.ClassType.class);
        return classTypeEntity;
    }

    public ClassType convertToModel(com.blockone.enrollment.entity.ClassType classTypeEntity) {
        ClassType classTypeModel = modelMapper.map(classTypeEntity, ClassType.class);
        return classTypeModel;
    }

}
