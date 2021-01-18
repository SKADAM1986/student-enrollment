package com.blockone.enrollment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
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

    @Column(name="enrollment_date", nullable = true)
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate enrollmentDate;

    @Column(name="last_update_date")
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate lastUpdateDate;

    @Column(name="active_indicator")
    private boolean activeIndicator;

}
