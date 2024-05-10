package com.example.hkproject.thirdParty;

import com.example.hkproject.entity.GeoPoint;
import org.springframework.stereotype.Component;

@Component
public class GoogleMapService {

    // 根据传入的经纬度，调用Google Map API获取两点之间的距离，距离以米为单位，返回整数
    public int getDistanceMeter(GeoPoint origin, GeoPoint destination) {
        if (origin == null || destination == null) {
            return 0;
        }
        // 调用Google Map API获取两点之间的距离
        return 10;
    }
}
