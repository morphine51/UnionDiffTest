package com.example.demo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableScheduling
public class TriggerController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
//	@RequestMapping("/trigger")
//	@Scheduled(fixedDelay = 1000)
	public void aaa() {
		
        System.out.println("========================================================");
        String baseQuery = "select customer, customer2, customer3, customer4, customer5 from dummy ";
        String query1 = baseQuery + "WHERE ID <= 1200000";
        String query2 = baseQuery + "WHERE ID >= 600001 AND ID <= 2400000";
        
        //minus
//        String minusQuery = query2 + " minus " + query1;
        
        HashSet<Map<String, Object>> listA = new HashSet<>(jdbcTemplate.queryForList(query1));
        HashSet<Map<String, Object>> listB = new HashSet<>(jdbcTemplate.queryForList(query2));
        
//        HashSet<Map<String, Object>> listC = new HashSet<>(jdbcTemplate.queryForList(query + "WHERE ID <= 1500000"));
//        HashSet<Map<String, Object>> listD = new HashSet<>(jdbcTemplate.queryForList(query + "WHERE ID <= 1500000"));
//        HashSet<Map<String, Object>> listE = new HashSet<>(jdbcTemplate.queryForList(query + "WHERE ID <= 1500000"));
        
        System.out.println("========================================================");
        System.out.println("HashSet1 size = " + listA.size());
        System.out.println("HashSet2 size = " + listB.size());
        System.out.println("==================================================");
        
        HashSet<Map<String, Object>> hashSet1 = new HashSet<>();
        HashSet<Map<String, Object>> hashSet2 = new HashSet<>();
        
        // 합집합
        hashSet1.clear();
        hashSet1.addAll(listA);
        hashSet2.clear();
        hashSet2.addAll(listB);
        long start = System.currentTimeMillis();
        
        hashSet2.addAll(hashSet1);
        System.out.println("합집합 - hashSet2.addAll(hashSet1); : " + (System.currentTimeMillis() - start) + "ms");
        System.out.println("합집합 Size = " + hashSet2.size());
        
        System.out.println("==================================================");
        
        // 교집합
        hashSet1.clear();
        hashSet1.addAll(listA);
        hashSet2.clear();
        hashSet2.addAll(listB);
        long start2 = System.currentTimeMillis();
        hashSet2.retainAll(hashSet1);
        System.out.println("교집합 - hashSet2.retainAll(hashSet1); : " + (System.currentTimeMillis() - start2) + "ms");
        System.out.println("교집합 Size = " + hashSet2.size());
        
        System.out.println("==================================================");
        
        // 차집합
        hashSet1.clear();
        hashSet1.addAll(listA);
        hashSet2.clear();
        hashSet2.addAll(listB);
        long start3 = System.currentTimeMillis();
        hashSet2.removeAll(hashSet1);
        System.out.println("차집합 - hashSet2.removeAll(hashSet1);: " + (System.currentTimeMillis() - start3) + "ms");
        System.out.println("차집합 Size = " + hashSet2.size());
        
        System.out.println("==================================================");
		
	}
	
}
