package com.blockone.enrollment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Enrollment implements Serializable {

    private Student student;
    private Semester semester;
    private ClassType classType;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate enrollmentDate;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate lastUpdateDate;

    private boolean activeIndicator;

    public EnrollmentId getEnrollmentId() {
        return new EnrollmentId(this.student.getStudentId(), this.semester.getSemesterId(), this.classType.getClassName());
    }

}
