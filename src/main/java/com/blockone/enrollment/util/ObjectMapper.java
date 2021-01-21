package com.blockone.enrollment.util;

import com.blockone.enrollment.entity.EnrollmentId;
import com.blockone.enrollment.models.ClassType;
import com.blockone.enrollment.models.Enrollment;
import com.blockone.enrollment.models.Semester;
import com.blockone.enrollment.models.Student;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapper {

    @Autowired
    ModelMapper modelMapper;

    private final Logger log = LoggerFactory.getLogger(ObjectMapper.class);

    public com.blockone.enrollment.entity.Enrollment convertToEntity(Enrollment enrollmentModel) {
        log.info("ObjectMapper.convertToEntity - enrollmentModel [{}]",enrollmentModel.getEnrollmentId());
        com.blockone.enrollment.entity.Enrollment enrollmentEntity = modelMapper.map(enrollmentModel, com.blockone.enrollment.entity.Enrollment.class);
        enrollmentEntity.setEnrollmentId(new EnrollmentId(
                this.convertToEntity(enrollmentModel.getStudent()),
                this.convertToEntity(enrollmentModel.getSemester()),
                this.convertToEntity(enrollmentModel.getClassType()))
        );
        log.info("ObjectMapper.convertToEntity - enrollmentEntity [{}]", enrollmentEntity.getEnrollmentId().getEnrollmentIdToPrint());
        return enrollmentEntity;
    }

    public Enrollment convertToModel(com.blockone.enrollment.entity.Enrollment enrollmentEntity) {
        log.info("ObjectMapper.convertToModel - enrollmentEntity [{}]", enrollmentEntity.getEnrollmentId().getEnrollmentIdToPrint());
        Enrollment enrollmentModel = modelMapper.map(enrollmentEntity, Enrollment.class);
        enrollmentModel.setStudent(this.convertToModel(enrollmentEntity.getEnrollmentId().getStudent()));
        enrollmentModel.setSemester(this.convertToModel(enrollmentEntity.getEnrollmentId().getSemester()));
        enrollmentModel.setClassType(this.convertToModel(enrollmentEntity.getEnrollmentId().getClassType()));
        log.info("ObjectMapper.convertToModel - enrollmentModel [{}]", enrollmentModel.getEnrollmentId());
        return enrollmentModel;
    }

    public com.blockone.enrollment.entity.Student convertToEntity(Student studentModel) {
        log.info("ObjectMapper.convertToEntity - studentModel [{}]", studentModel.getStudentId());
        com.blockone.enrollment.entity.Student studentEntity = modelMapper.map(studentModel, com.blockone.enrollment.entity.Student.class);
        log.info("ObjectMapper.convertToEntity - studentEntity [{}]", studentEntity.getStudentId());
        return studentEntity;
    }

    public Student convertToModel(com.blockone.enrollment.entity.Student studentEntity) {
        log.info("ObjectMapper.convertToModel - studentEntity [{}]", studentEntity.getStudentId());
        Student studentModel = modelMapper.map(studentEntity, Student.class);
        log.info("ObjectMapper.convertToModel - studentModel [{}]", studentModel.getStudentId());
        return studentModel;
    }

    public com.blockone.enrollment.entity.Semester convertToEntity(Semester semesterModel) {
        return modelMapper.map(semesterModel, com.blockone.enrollment.entity.Semester.class);
    }

    public Semester convertToModel(com.blockone.enrollment.entity.Semester semesterEntity) {
        return modelMapper.map(semesterEntity, Semester.class);
    }

    public com.blockone.enrollment.entity.ClassType convertToEntity(ClassType classTypeModel) {
        return modelMapper.map(classTypeModel, com.blockone.enrollment.entity.ClassType.class);
    }

    public ClassType convertToModel(com.blockone.enrollment.entity.ClassType classTypeEntity) {
        return modelMapper.map(classTypeEntity, ClassType.class);
    }

}
