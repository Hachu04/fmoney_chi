package com.example.fmgatewayvpbank.chiho.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VPBankTokenService {

    @Value("${vpbank.token.url}")
    private String tokenUrl;

    @Value("${vpbank.app.code}")
    private String appCode;

    private String token;
    private LocalDateTime expirationTime;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getToken() {
        if (token == null || LocalDateTime.now().isAfter(expirationTime)) {
            requestNewToken();
        }
        return token;
    }

    public void requestNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("username", "password");
        headers.set("IDN-App", appCode);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("scope", "init_payments_data_read make_internal_transfer make_external_fund_transfer own_transfer_history_read");
        params.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl + "/token", request, Map.class);

        this.token = (String) response.getBody().get("access_token");
        int expiresIn = (int) response.getBody().get("expires_in");
        this.expirationTime = LocalDateTime.now().plusSeconds(expiresIn);
    }
}

