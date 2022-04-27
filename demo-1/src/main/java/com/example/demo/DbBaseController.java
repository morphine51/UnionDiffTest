package com.example.demo;

import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import redis.clients.jedis.Jedis;

@Controller
public class DbBaseController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@RequestMapping("db")
	public ResponseEntity<HttpStatus> db() {
		
        String baseQuery = "select customer, customer2, customer3, customer4, customer5 from dummy ";
        String query1 = baseQuery + "WHERE ID <= 1200000";
        String query2 = baseQuery + "WHERE ID >= 600001 AND ID <= 2400000";
        
        String plusQuery = query2 + " union " + query1; // 중복제거
//        String plusQuery = query2 + " union all" + query1; // 중복허용
        String minusQuery = query2 + " minus " + query1;
        
        System.out.println("==================================================");
        // 합집합
        long start = System.currentTimeMillis();
        HashSet<Map<String, Object>> listA = new HashSet<>(jdbcTemplate.queryForList(plusQuery));
        System.out.println("합집합 - hashSet2.addAll(hashSet1); : " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("합집합 Size = " + listA.size());
        System.out.println("==================================================");
        // 차집합
        long start2 = System.currentTimeMillis();
        HashSet<Map<String, Object>> listB = new HashSet<>(jdbcTemplate.queryForList(minusQuery));
        System.out.println("차집합 - hashSet2.removeAll(hashSet1);: " + (System.currentTimeMillis() - start2) + "ms");
        System.out.println("차집합 Size = " + listB.size());
        System.out.println("==================================================");
        
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		
	}
}
