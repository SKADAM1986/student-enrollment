package com.blockone.enrollment.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse implements Serializable {

    int status;
    String error;
    String message;

}
