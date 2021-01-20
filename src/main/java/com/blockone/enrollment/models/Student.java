package com.blockone.enrollment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Student  implements Serializable {
    private Long studentId;

    private String firstName;

    private String lastName;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate dateOfBirth;

    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate createDate;

    private String phone;

    private String nationality;

}
