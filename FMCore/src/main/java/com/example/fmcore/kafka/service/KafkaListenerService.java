package com.example.fmcore.kafka.service;

import com.example.fmcore.chiho.entity.ChiHoResponse;
import com.example.fmcore.chiho.repository.ChiHoResponseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaListenerService {

    @Autowired
    private ChiHoResponseRepository chiHoResponseRepository;

    @KafkaListener(topics = "chi_ho_response", groupId = "fmcore_2024")
    public void listen(String message) {
        // Parse message to extract signature and status
        // Assuming message is in JSON format: {"signature":"signature_value", "status":"status_value"}
        // Use a JSON library to parse the message
        // Example with Jackson:
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode node = objectMapper.readTree(message);
            String signature = node.get("signature").asText();
            String status = node.get("status").asText();

            ChiHoResponse response = new ChiHoResponse();
            response.setSignature(signature);
            response.setStatus(status);

            chiHoResponseRepository.save(response);
        } catch (IOException e) {
            // Handle parsing exception
        }
    }
}
