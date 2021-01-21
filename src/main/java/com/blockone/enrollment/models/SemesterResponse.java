package com.blockone.enrollment.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SemesterResponse extends AppResponse implements Serializable {
    private Long semesterId;

    public SemesterResponse(Long semesterId, String message) {
        super(message);
        this.semesterId = semesterId;
    }

}
