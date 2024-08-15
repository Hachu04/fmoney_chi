package com.example.fmgatewayvcb.chiho.service.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Random;

@Service
public class KafkaListenerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @KafkaListener(topics = "chi_ho_request_VCB", groupId = "fmgw_2024")
    public void listen(String message) {
        System.out.println(message);
        try {
            JsonNode node = objectMapper.readTree(message);

            // Extract data from Kafka message
            String customerCode = node.get("customerCode").asText();
            String bank = node.get("bank").asText();
            String targetNumber = node.get("targetNumber").asText();
            String channelId = node.get("channelId").asText();
            String channelRefNumber = node.get("channelRefNumber").asText();
            String requestDateTime = node.get("requestDateTime").asText();
            String internalTransactionRefNo = node.get("internalTransactionRefNo").asText();
            String paymentMode = node.get("paymentMode").asText();
            String totalPaymentAmount = node.get("totalPaymentAmount").asText();
            String serviceId = node.get("serviceId").asText();
            String messageSignature = node.get("signature").asText(); // Extract signature from message

            // Generate a random signature
            int randomSignature = random.nextInt(10000) + 1; // Range from 1 to 10000
            String signature = Integer.toString(randomSignature); // Set signature to a random number

            // Create the new request payload
            ObjectNode requestPayload = objectMapper.createObjectNode();
            ObjectNode contextNode = objectMapper.createObjectNode();
            contextNode.put("channelId", channelId);
            contextNode.put("channelRefNumber", channelRefNumber);
            contextNode.put("requestDateTime", requestDateTime);

            ObjectNode payloadNode = objectMapper.createObjectNode();
            payloadNode.put("customerCode", customerCode);
            payloadNode.put("internalTransactionRefNo", internalTransactionRefNo);
            payloadNode.put("paymentMode", paymentMode);
            payloadNode.put("providerId", bank); // Use bank as providerId
            payloadNode.put("totalPaymentAmount", totalPaymentAmount);
            payloadNode.put("serviceId", serviceId);

            requestPayload.set("context", contextNode);
            requestPayload.set("payload", payloadNode);
            requestPayload.put("signature", signature);

            // Send the new request
            String apiUrl = "http://localhost:6000/api/v1/chi_ho";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestPayload.toString(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            ObjectNode responseBody = objectMapper.readValue(responseEntity.getBody(), ObjectNode.class);

            // Create the response payload
            ObjectNode responsePayload = objectMapper.createObjectNode();
            responsePayload.put("signature", messageSignature); // Use the signature from the original message
            responsePayload.put("status", responseBody.get("status").asText()); // Status from response

            //TODO
            System.out.println("request payload: " + requestPayload.toString());
            System.out.println("response payload: " + responsePayload.toString());
            // Send the response to Kafka topic
            kafkaTemplate.send("chi_ho_response", responsePayload.toString());

        } catch (IOException e) {
            e.fillInStackTrace(); // Log exception
        }
    }
}
