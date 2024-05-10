package com.example.hkproject.dto;

import lombok.Data;

@Data
public class TakeOrderResp {
    private String status;

    public TakeOrderResp(String status) {
        this.status = status;
    }
}
