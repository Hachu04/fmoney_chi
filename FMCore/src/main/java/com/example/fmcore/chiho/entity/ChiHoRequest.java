package com.example.fmcore.chiho.entity;

import jakarta.persistence.Entity;


@Entity
public class ChiHoRequest {
    @jakarta.persistence.Id
    private String signature;

    private String customerCode;
    private String bank;
    private String targetNumber;
    private String channelId;
    private String channelRefNumber;
    private String requestDateTime;
    private String internalTransactionRefNo;
    private String paymentMode;
    private String totalPaymentAmount;
    private String serviceId;

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelRefNumber() {
        return channelRefNumber;
    }

    public void setChannelRefNumber(String channelRefNumber) {
        this.channelRefNumber = channelRefNumber;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    public String getInternalTransactionRefNo() {
        return internalTransactionRefNo;
    }

    public void setInternalTransactionRefNo(String internalTransactionRefNo) {
        this.internalTransactionRefNo = internalTransactionRefNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(String totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
