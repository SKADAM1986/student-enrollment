package com.blockone.enrollment.models;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class EnrollmentId implements Serializable, Comparable<EnrollmentId> {

    private Long studentId;
    private Long semesterId;
    private String className;

    @Override
    public int compareTo(EnrollmentId o) {

        int i = this.getStudentId().compareTo(o.getStudentId());
        if (i != 0) return i;

        i = this.getSemesterId().compareTo(o.getSemesterId());
        if (i != 0) return i;

        i = this.getClassName().compareTo(o.getClassName());
        if (i != 0) return i;

        return 1;
    }
}
