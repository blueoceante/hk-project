package com.example.hkproject.thirdParty;

import com.example.hkproject.entity.GeoPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleMapService {
    private final String MAP_URL_PREFIX = "https://maps.googleapis.com/maps/api/distancematrix/json";
    @Value("${google.api-key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Exception exception = new Exception("Google Map API error");

    // 根据传入的经纬度，调用Google Map API获取两点之间的距离，距离以米为单位，返回整数
    public int getDistanceMeter(GeoPoint origin, GeoPoint destination) throws Exception {
        String originStr = this.transformGeoPoint(origin); // 替换为起始地点的经纬度
        String destinationStr = this.transformGeoPoint(destination); // 替换为目的地的经纬度
        String url = String.format("%s?origins=%s&destinations=%s&key=%s", MAP_URL_PREFIX, originStr, destinationStr, apiKey);

        GoogleDistanceMatrixResp response = restTemplate.getForObject(url, GoogleDistanceMatrixResp.class);

        if (response == null || response.getStatus() == null || !response.getStatus().equals("OK") ||
                response.getRows() == null || response.getRows().length == 0) {
            throw exception;
        }
        GoogleDistanceMatrixResp.Row row = response.getRows()[0];
        if (row.getElements() == null || row.getElements().length == 0) {
            throw exception;
        }
        // 只关心第一行第一个元素的距离
        GoogleDistanceMatrixResp.Element element = response.getRows()[0].getElements()[0];
        if (element.getStatus() == null || !element.getStatus().equals("OK")) {
            throw exception;
        }
        return element.getDistance().getValue();
    }

    private String transformGeoPoint(GeoPoint geoPoint) throws Exception {
        if (geoPoint == null || geoPoint.getLatitude() == null || geoPoint.getLongitude() == null) {
            throw new Exception("Invalid GeoPoint");
        }
        return geoPoint.getLatitude().toString() + "," + geoPoint.getLongitude().toString();
    }
}
