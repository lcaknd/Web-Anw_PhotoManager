package com.example.demo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {

    private String status;

    private String message;

    private Object data;

    private String errorMessages;

    public MessageResponse(String status, String errorMessages) {
        this.status = status;
        this.errorMessages = errorMessages;
    }

    public MessageResponse(String status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
