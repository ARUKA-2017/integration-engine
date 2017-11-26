package com.akura.mapping.models;

/**
 * Class representing a ServiceResponse.
 */
public class ServiceResponse {
    public String status;
    public String message;

    public ServiceResponse(String status, String msg) {
        this.status = status;
        this.message = msg;
    }
}
