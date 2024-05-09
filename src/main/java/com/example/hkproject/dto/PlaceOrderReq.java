package com.example.hkproject.dto;

import com.example.hkproject.validation.restReqParam.LatLongValid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PlaceOrderReq {
    @NotNull
    @Size(min = 2, max = 2)
    @LatLongValid
    private String[] origin;

    @NotNull
    @Size(min = 2, max = 2)
    @LatLongValid
    private String[] destination;
}
