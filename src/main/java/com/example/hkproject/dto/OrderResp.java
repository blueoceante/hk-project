package com.example.hkproject.dto;

import lombok.Data;

@Data
public class OrderResp {
    private long id;
    private int distance;
    private String status;
}
