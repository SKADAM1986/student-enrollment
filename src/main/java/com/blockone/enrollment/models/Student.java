package com.blockone.enrollment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="students")
public class Student  implements Serializable {
    @Id @GeneratedValue @Column(name="student_id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="birth_date")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateOfBirth;

    @Column(name="create_date", columnDefinition="DATE DEFAULT CURRENT_DATE")
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate createDate;

    @Column(name="phone_no")
    private String phone;

    @Column(name="nationality")
    private String nationality;

}
