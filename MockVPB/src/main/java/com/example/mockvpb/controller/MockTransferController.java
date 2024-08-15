package com.example.mockvpb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/mock/vpbank")
public class MockTransferController {

    @PostMapping("/payments/v1/transfer/internal")
    public ResponseEntity<Map<String, Object>> transferMoney(
            @RequestHeader("x-request-id") String requestId,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader("IDN-App") String appCode,
            @RequestBody Map<String, Object> request) {

        Map<String, Object> response = new HashMap<>();
        response.put("referenceNumber", request.get("referenceNumber"));
        response.put("transactionId", UUID.randomUUID().toString());
        response.put("transferResult", "complete");
        response.put("transactionDate", "2023-07-01 12:34:56");
        response.put("tranferDate", "2023-07-01");
        response.put("signature", Base64.getEncoder().encodeToString(
                (request.get("referenceNumber") + UUID.randomUUID().toString() + "2023-07-01").getBytes()
        ));
        return ResponseEntity.ok(response);
    }
}

