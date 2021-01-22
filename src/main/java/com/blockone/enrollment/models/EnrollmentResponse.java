package com.blockone.enrollment.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EnrollmentResponse extends AppResponse implements Serializable {
    private Long studentId;
    private Long semesterId;
    private String className;

    public EnrollmentResponse(Long studentId,Long semesterId, String className, String message) {
        super(message);
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.className = className;
    }

}
