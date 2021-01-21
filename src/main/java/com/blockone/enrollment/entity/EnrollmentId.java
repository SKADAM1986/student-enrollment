package com.blockone.enrollment.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class EnrollmentId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "class_name")
    private ClassType classType;

    public String getEnrollmentIdToPrint() {

        return "EnrollmentId{" +
                "studentId=" + student.getStudentId() +
                ", semesterId=" + semester.getSemId() +
                ", className=" + classType.getClassName() +
                '}';
    }

}
