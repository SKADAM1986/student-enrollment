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
public class Semester implements Serializable {

    private Long semesterId;

    private String semesterName;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate endDate;

}
