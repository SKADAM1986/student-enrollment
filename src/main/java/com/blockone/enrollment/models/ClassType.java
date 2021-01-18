package com.blockone.enrollment.models;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="class_info")
public class ClassType  implements Serializable {

    @Id
    @Column(name = "class_name", columnDefinition="VARCHAR(45)")
    private String className;

    @Column(name="credits")
    private int creditPoints;

}
