package com.blockone.enrollment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name="enrollment")
public class Enrollment  implements Serializable {

    @EmbeddedId
    EnrollmentId enrollmentId;

    @Column(name="enrollment_date")
    @JsonFormat(pattern="MM/dd/yyyy")
    private LocalDate enrollmentDate;

    @Column(name="last_update_date")
    private LocalDate lastUpdateDate;

    @Column(name="active_indicator")
    private boolean activeIndicator;


}
