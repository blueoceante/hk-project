package com.example.hkproject.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderTab {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer orderStatus;
    private Integer distance;
    private BigDecimal originLongitude;
    private BigDecimal originLatitude;
    private BigDecimal destLongitude;
    private BigDecimal destLatitude;
    private Integer ctime;
    private Integer mtime;
    private Integer isDel;
}