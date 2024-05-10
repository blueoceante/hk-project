package com.example.hkproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TakeOrderReq {
    @Pattern(regexp = "TAKEN")
    private String status;
}
