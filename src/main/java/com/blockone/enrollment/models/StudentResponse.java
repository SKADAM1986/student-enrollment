package com.blockone.enrollment.models;

import lombok.*;
import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class StudentResponse extends AppResponse implements Serializable {
    private Long studentId;

    public StudentResponse(Long studentId, String message) {
        super(message);
        this.studentId = studentId;
    }

}
