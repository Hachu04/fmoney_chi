package com.example.mockvpb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/mock/vpbank")
public class MockTokenController {

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(@RequestParam String scope, @RequestParam String grant_type) {
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", UUID.randomUUID().toString());
        response.put("scope", scope);
        response.put("token_type", "Bearer");
        response.put("expires_in", 3600);
        return ResponseEntity.ok(response);
    }
}

