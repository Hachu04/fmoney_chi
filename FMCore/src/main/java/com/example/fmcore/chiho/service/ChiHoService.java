package com.example.fmcore.chiho.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fmcore.chiho.entity.ChiHoRequest;
import com.example.fmcore.chiho.entity.ChiHoResponse;
import com.example.fmcore.chiho.repository.ChiHoRepository;
import com.example.fmcore.chiho.repository.ChiHoResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ChiHoService {

    @Autowired
    private ChiHoRepository chiHoRepository;

    @Autowired
    private ChiHoResponseRepository chiHoResponseRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<Map<String, Object>> processRequest(ChiHoRequest request) {
        // Check if bank is supported
        if (!"VCB".equals(request.getBank()) && !"VPBank".equals(request.getBank())) {
            return ResponseEntity.badRequest().body(Map.of("status", false, "msg", "bank not supported"));
        }

        // Check if signature already exists
        Optional<ChiHoResponse> existingResponse = chiHoResponseRepository.findById(request.getSignature());
        if (existingResponse.isPresent()) {
            return ResponseEntity.ok(Map.of("status", true, "msg", existingResponse.get().getStatus()));
        }

        // Save to database
        ChiHoRequest chiHoRequest = new ChiHoRequest();
        chiHoRequest.setSignature(request.getSignature());
        chiHoRequest.setCustomerCode(request.getCustomerCode());
        chiHoRequest.setBank(request.getBank());
        chiHoRequest.setTargetNumber(request.getTargetNumber());
        chiHoRequest.setChannelId(request.getChannelId());
        chiHoRequest.setChannelRefNumber(request.getChannelRefNumber());
        chiHoRequest.setRequestDateTime(request.getRequestDateTime());
        chiHoRequest.setInternalTransactionRefNo(request.getInternalTransactionRefNo());
        chiHoRequest.setPaymentMode(request.getPaymentMode());
        chiHoRequest.setTotalPaymentAmount(request.getTotalPaymentAmount());
        chiHoRequest.setServiceId(request.getServiceId());

        chiHoRepository.save(chiHoRequest);

        // Convert ChiHoRequest to JSON
        String requestJson;
        try {
            requestJson = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("status", false, "msg", "error processing request"));
        }

        // Send to Kafka
        String topic = "VCB".equals(request.getBank()) ? "chi_ho_request_VCB" : "chi_ho_request_VPBank";
        kafkaTemplate.send(topic, requestJson);
        System.out.println(requestJson);

        return ResponseEntity.ok(Map.of("status", true, "msg", "request accepted"));
    }
}

