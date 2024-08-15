package com.example.fmcore.chiho.entity;

import jakarta.persistence.Entity;

@Entity
public class ChiHoResponse {

    @jakarta.persistence.Id
    private String signature; // Primary Key

    private String status;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getters and Setters
}
