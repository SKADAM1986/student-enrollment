package com.blockone.enrollment.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="semester")
public class Semester implements Serializable {

    @Id @GeneratedValue @Column(name="semester_id")
    private Long semesterId;

    @Column(name="semester_name")
    private String semesterName;

    @Column(name="start_date")
    private LocalDate startDate;

    @Column(name="end_date")
    private LocalDate endDate;

}
