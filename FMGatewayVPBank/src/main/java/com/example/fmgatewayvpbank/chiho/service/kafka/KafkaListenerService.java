package com.example.fmgatewayvpbank.chiho.service.kafka;

import com.example.fmgatewayvpbank.chiho.service.VPBankBeneficiaryService;
import com.example.fmgatewayvpbank.chiho.service.VPBankTokenService;
import com.example.fmgatewayvpbank.chiho.service.VPBankTransferService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class KafkaListenerService {

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private final VPBankTokenService vpBankTokenService;

    @Autowired
    private final VPBankBeneficiaryService vpBankBeneficiaryService;

    @Autowired
    private final VPBankTransferService vpBankTransferService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Random random = new Random();

    public KafkaListenerService(VPBankTransferService vpBankTransferService, VPBankBeneficiaryService vpBankBeneficiaryService, VPBankTokenService vpBankTokenService, KafkaTemplate<String, String> kafkaTemplate, RestTemplate restTemplate) {
        this.vpBankTransferService = vpBankTransferService;
        this.vpBankBeneficiaryService = vpBankBeneficiaryService;
        this.vpBankTokenService = vpBankTokenService;
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "chi_ho_request_VPBank", groupId = "fmgwvp_2024")
    public void listenVPBank(String message) {
        try {
            JsonNode request = objectMapper.readTree(message);

            // Extract data from Kafka message
            String customerCode = request.get("customerCode").asText();
            String targetNumber = request.get("targetNumber").asText();
            String messageSignature = request.get("signature").asText();

            // Generate a random signature
            int randomSignature = random.nextInt(10000) + 1; // Range from 1 to 10000
            String signature = Integer.toString(randomSignature);

            // Get token
            String token = vpBankTokenService.getToken();

            // Check customer number
            boolean isCustomerValid = vpBankBeneficiaryService.checkCustomerNumber(token, customerCode, targetNumber);

            ObjectNode responsePayload = objectMapper.createObjectNode();
            responsePayload.put("signature", messageSignature); // Use the signature from the original message

            if (!isCustomerValid) {
                responsePayload.put("status", "403 Forbidden");
                responsePayload.put("error", "Customer info mismatch");

                kafkaTemplate.send("chi_ho_response", responsePayload.toString());
                return; // Exit
            }

            // Transfer money if customer valid
            vpBankTransferService.transferMoney(token, request);

            String apiUrl = "http://localhost:6000/api/v1/chi_ho";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(request.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            responsePayload.put("signature", messageSignature); // Use the signature from the original message
            responsePayload.put("status", responseEntity.getStatusCode().toString()); // Status from response

            //TODO
            System.out.println(request);
            // Send the response to Kafka topic
            kafkaTemplate.send("chi_ho_response", responsePayload.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
