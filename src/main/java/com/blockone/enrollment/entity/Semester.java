package com.blockone.enrollment.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="semester")
public class Semester implements Serializable {

    @Id @GeneratedValue @Column(name="sem_id")
    private Long semId;

    @Column(name="sem_name")
    private String semName;

    @Column(name="start_date")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date startDate;

    @Column(name="end_date")
    @DateTimeFormat(pattern="MM/dd/yyyy")
    private Date endDate;

}
