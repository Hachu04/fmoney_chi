package com.example.fmgatewayvpbank.chiho.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class VPBankTransferService {

    private final RestTemplate restTemplate;

    @Value("${vpbank.base.url}")
    private String baseUrl;

    @Value("${vpbank.app.code}")
    private String appCode;

    public VPBankTransferService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void transferMoney(String token, JsonNode request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("x-request-id", "some-request-id");
        headers.set("IDN-App", appCode);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> transferRequest = new HashMap<>();
        transferRequest.put("referenceNumber", request.get("internalTransactionRefNo").asText());
        transferRequest.put("service", "internal");
        transferRequest.put("transactionType", "CACA");
        transferRequest.put("sourceNumber", "232506601");
        transferRequest.put("targetNumber", request.get("targetNumber").asText());
        transferRequest.put("amount", request.get("totalPaymentAmount").asDouble());
        transferRequest.put("remark", "NOI DUNG CHUYEN KHOAN");
        transferRequest.put("signature", "SIGNATURE TEST");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(transferRequest, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                baseUrl + "/payments/v1/transfer/internal", HttpMethod.POST, entity, JsonNode.class
        );

        System.out.println(response.getBody());
    }
}


