package com.example.cinema_tickets.dto;

public class ApiResponse {
    private String status;
    private String message;
    private int totalAmount;

    public ApiResponse() {
    }
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.totalAmount = 0;
    }
    public ApiResponse(String status, String message, int totalAmount) {
        this.status = status;
        this.message = message;
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
