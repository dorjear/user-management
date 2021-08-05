package com.example.usermanagement.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiExceptionHandlerTest {

    @Test
    void handleEntityNotFoundException() {
            var handler = new ApiExceptionHandler();
            var responseEntity =handler.handleEntityNotFoundException(new EntityNotFoundException());
            assertEquals(HttpStatus.UNAUTHORIZED,responseEntity.getStatusCode());

    }
}