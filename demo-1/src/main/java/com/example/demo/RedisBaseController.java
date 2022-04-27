package com.example.demo;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

@Controller
public class RedisBaseController {

	@Autowired
	private Jedis jedis;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@RequestMapping("redis")
	public @ResponseBody ResponseEntity<HttpStatus> bbb() {
		clearRedis();

		Long processStartTime = System.currentTimeMillis();
		
		JSONArray jsonArray1 = this.query("select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID <= 1200000");
		insertRedisPipeline("key1", jsonArray1);
		jsonArray1.clear();

		JSONArray jsonArray2 = this.query("select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID >= 600001 AND ID <= 2400000");
		insertRedisPipeline("key2", jsonArray2);
		jsonArray2.clear();
		
		JSONArray jsonArray3 = this.query("select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID <= 1200000");
		insertRedisPipeline("key3", jsonArray3);
		jsonArray3.clear();

		JSONArray jsonArray4 = this.query("select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID >= 600001 AND ID <= 2400000");
		insertRedisPipeline("key4", jsonArray4);
		jsonArray4.clear();

		// 6. Redis에서 집합연산
		Long start = System.currentTimeMillis();
		Long a = jedis.sunionstore("tot", "key2", "key1");
//		System.out.println("합집합 결과 = " + sunion.size() + " / 시간 = " + (System.currentTimeMillis() - start) + "ms");
		System.out.println("합집합 결과 시간 = " + (System.currentTimeMillis() - start) + "ms" + " " + a);
		
		// 6. Redis에서 집합연산
		start = System.currentTimeMillis();
		jedis.sdiffstore("tot", "tot", "key3");
		System.out.println("차집합 결과 시간 = " + (System.currentTimeMillis() - start) + "ms");
		// 기대값 = 1200000
		Set<String> tot = jedis.smembers("tot");
		System.out.println("########### : " + tot.size());
		
		// 6. Redis에서 집합연산
		start = System.currentTimeMillis();
		jedis.sunionstore("tot", "tot", "key4");
		System.out.println("합집합 결과 시간 = " + (System.currentTimeMillis() - start) + "ms");
		
		// 8. Redis에서 결과 날리기
		clearRedis();		
		System.out.println("전체걸린 시간 = " + (System.currentTimeMillis() - processStartTime) + "ms");
		
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
	
	private void clearRedis() {
		
		jedis.del("key1");
		jedis.del("key2");
		jedis.del("key3");
		jedis.del("key4");
		jedis.del("tot");
	}
	
	public JSONArray query(String query) {
		Long start = System.currentTimeMillis();
		JSONArray jsonArray1 = new JSONArray(jdbcTemplate.queryForList(query));
		System.out.println("Query Select Duration : " + (System.currentTimeMillis() - start) + "ms");
		return jsonArray1;
	}

	public void insertRedisPipeline(String key, JSONArray jsonArray) {

		System.out.println("==Redis Pipeline Insert Start == array size=" + jsonArray.length());
		Long start = System.currentTimeMillis();
		
		Pipeline pipeline = jedis.pipelined();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			pipeline.sadd(key, jsonObject.toString());
		}
		pipeline.sync();
		
		System.out.println("Redis Pipeline Insert Duration : " + (System.currentTimeMillis() - start) + "ms");

	}
}
