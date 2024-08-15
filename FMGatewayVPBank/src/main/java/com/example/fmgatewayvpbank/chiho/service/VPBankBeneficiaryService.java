package com.example.fmgatewayvpbank.chiho.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class VPBankBeneficiaryService {

    @Value("${vpbank.base.url}")
    private String baseUrl;

    @Value("${vpbank.app.code}")
    private String appCode;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean checkCustomerNumber(String token, String customerNumber, String targetNumber) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("IDN-App", appCode);
        headers.set("x-request-id", UUID.randomUUID().toString());

        HttpEntity<String> request = new HttpEntity<>(headers);

        String url = baseUrl + "/payments/v1/beneficiary/info?benType=internal&benNumber=" + targetNumber;

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
        String fetchedCustomerNumber = (String) response.getBody().get("customerNumber");

        return customerNumber.equals(fetchedCustomerNumber);
    }
}

