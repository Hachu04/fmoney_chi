package com.example.fmcore.chiho.controller;

import com.example.fmcore.chiho.entity.ChiHoRequest;
import com.example.fmcore.chiho.service.ChiHoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ChiHoController {

    @Autowired
    private ChiHoService chiHoService;

    @PostMapping("/chi_ho")
    public ResponseEntity<Map<String, Object>> handleChiHoRequest(@RequestBody ChiHoRequest request) {
        return chiHoService.processRequest(request);
    }
}
