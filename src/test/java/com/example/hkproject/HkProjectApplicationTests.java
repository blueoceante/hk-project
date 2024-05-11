package com.example.hkproject;

import com.example.hkproject.dto.PlaceOrderReq;
import com.example.hkproject.dto.TakeOrderReq;
import com.example.hkproject.entity.GeoPoint;
import com.example.hkproject.exception.TakeOrderException;
import com.example.hkproject.thirdParty.GoogleMapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HkProjectApplicationTests { // 集成测试类
	@MockBean
	private GoogleMapService googleMapsService;

	@Autowired
    private MockMvc mockMvc;

	private static final String urlPrefix = "/orders";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@AfterEach
	public void cleanUpEach() {
		jdbcTemplate.execute("DROP TABLE IF EXISTS order_tab");
	}


	// 全部接口集成测试流程
	@Test
	void testEntireApplicationNormal() throws Exception {
		// 1.测试获取内存测试数据库中的订单列表
		this.performGetOrders()
				.andExpect(status().isOk())
				// 检查是不是一个数组
				.andExpect(jsonPath("$", isA(JSONArray.class)))
				// 检查数组里面的元素数量是不是2
				.andExpect(jsonPath("$", hasSize(2)))
				// 检查第一个元素的id是不是8
				.andExpect(jsonPath("$[0].id", is(8)))
				// 检查第一个元素的id是不是9
				.andExpect(jsonPath("$[1].id", is(9)));

		// 2.测试添加一条数据
		this.performPlaceOrder()
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10)) // 数据返回的订单ID是10
				.andExpect(jsonPath("$.distance").value(1000))
				.andExpect(jsonPath("$.status").value("UNASSIGNED"));

		// 3.测试获取内存测试数据库中的订单列表，看看是否有新的订单
		this.performGetOrders()
				.andExpect(status().isOk())
				// 检查是不是一个数组
				.andExpect(jsonPath("$", isA(JSONArray.class)))
				// 检查数组里面的元素数量是不是3
				.andExpect(jsonPath("$", hasSize(3)))
				// 检查新订单的id是不是10
				.andExpect(jsonPath("$[2].id", is(10)));

		// 4.测试taken新添加的订单
		this.performTakeOrder(10)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("SUCCESS"));

		// 5.再次测试taken新添加的订单，看看是否抛出异常
		this.performTakeOrder(10)
				.andExpect(status().is4xxClientError())
				.andExpect(result -> Assertions.assertInstanceOf(TakeOrderException.class, result.getResolvedException()));

	}

	private ResultActions performPlaceOrder() throws Exception {
		PlaceOrderReq mockReq = new PlaceOrderReq();
		mockReq.setOrigin(new String[]{"39.996794", "116.48105"});
		mockReq.setDestination(new String[]{"20.356786", "100.35105"});
		GeoPoint origin = GeoPoint.parse(mockReq.getOrigin());
		GeoPoint destination = GeoPoint.parse(mockReq.getDestination());
		int distance = 1000; // mock的距离，只mock google map api
		when(googleMapsService.getDistanceMeter(origin, destination)).thenReturn(distance);
		return mockMvc.perform(post("/orders")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(mockReq)));

	}

	private ResultActions performGetOrders() throws Exception {
		return mockMvc.perform(get(urlPrefix+ "?page=1&limit=10")
						.contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(new ArrayList<>())));
	}

	private ResultActions performTakeOrder(long orderId) throws Exception {
		return mockMvc.perform(patch(urlPrefix+ "/" + orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(new TakeOrderReq())));
	}



}
