package com.example.StudentProfile.exceptions;

import org.springframework.http.ResponseEntity;

public class ResponceEntityBuilder {
    public static ResponseEntity<Object> build (ApiExceptionModel apiExceptionModel) {
        return new ResponseEntity<>(apiExceptionModel, apiExceptionModel.getHttpStatus());
    }
}
