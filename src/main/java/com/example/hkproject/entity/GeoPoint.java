package com.example.hkproject.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class GeoPoint {
    private BigDecimal latitude;
    private BigDecimal longitude;

    public GeoPoint(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static GeoPoint parse(String[] latLong) {
        if (latLong == null || latLong.length != 2) {
            return null;
        }
        try {
            BigDecimal lat = new BigDecimal(latLong[0]).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lon = new BigDecimal(latLong[1]).setScale(6, RoundingMode.HALF_UP);
            return new GeoPoint(lat, lon);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
