package com.blockone.enrollment.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SemesterResponse extends AppResponse implements Serializable {
    private Long semesterId;

    public SemesterResponse(Long studentId, String message) {
        this.semesterId = studentId;
        this.setMessage(message);
    }

}
