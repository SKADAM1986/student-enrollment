package com.blockone.enrollment.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EnrollmentResponse extends AppResponse implements Serializable {
    private Long studentId;
    private Long semesterId;
    private String className;

    public EnrollmentResponse(Long studentId,Long semesterId, String className, String message) {
        this.studentId = studentId;
        this.semesterId = semesterId;
        this.className = className;
        this.setMessage(message);
    }

}
