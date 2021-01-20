package com.blockone.enrollment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Semester {

    private Long semId;

    private String semName;

    @JsonFormat(pattern="MM/dd/yyyy")
    private Date startDate;

    @JsonFormat(pattern="MM/dd/yyyy")
    private Date endDate;

}
