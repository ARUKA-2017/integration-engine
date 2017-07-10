package com.akura.mapping.models;


public class ServiceResponse {
    public String status;
    public String message;

    public ServiceResponse(String status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
