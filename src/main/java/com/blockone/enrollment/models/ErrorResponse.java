package com.blockone.enrollment.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    String errorMessage;
    String errorCode;

}
