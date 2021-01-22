package com.blockone.enrollment.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "class_name")
    private String className;

    @Column(name="credits")
    private int creditPoints;

}
