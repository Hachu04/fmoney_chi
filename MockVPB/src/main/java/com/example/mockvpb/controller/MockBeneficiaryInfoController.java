package com.example.mockvpb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mock/vpbank")
public class MockBeneficiaryInfoController {

    @GetMapping("/payments/v1/beneficiary/info")
    public ResponseEntity<Map<String, Object>> getBeneficiaryInfo(
            @RequestHeader("x-request-id") String requestId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("IDN-App") String appCode,
            @RequestParam String benType,
            @RequestParam String benNumber,
            @RequestHeader(value = "bankId", required = false) String bankId) {

        Map<String, Object> response = new HashMap<>();
        response.put("beneficiaryName", "VPBANK-640625");
        response.put("customerNumber", "640625");
        return ResponseEntity.ok(response);
    }
}

