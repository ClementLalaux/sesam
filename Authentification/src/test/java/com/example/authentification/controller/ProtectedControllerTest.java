package com.example.authentification.controller;

import com.example.authentification.controller.ProtectedController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtectedControllerTest {

    @Test
    void testGet() {

        ProtectedController controller = new ProtectedController();

        ResponseEntity<String> response = controller.get();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Ok", response.getBody());
    }
}
