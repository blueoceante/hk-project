package com.example.hkproject.thirdParty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleDistanceMatrixResp {
    private Row[] rows;
    private String status;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Row {
        private Element[] elements;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Element {
        private Distance distance;
        private String status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Distance {
        private int value;
        private String text;
    }

}
