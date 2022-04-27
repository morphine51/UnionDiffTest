package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DbBaseController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@RequestMapping("db")
	public ResponseEntity<HttpStatus> db() {
		
		System.out.println("DB Base Start ... ");
		Long processStartTime = System.currentTimeMillis();
		
		String query1 = "select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID <= 1200000";
		String query2 = "select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID >= 600001 AND ID <= 2400000";
		String query3 = "select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID <= 1200000";
		String query4 = "select customer, customer2, customer3, customer4, customer5 from dummy WHERE ID >= 600001 AND ID <= 2400000";
		
		String totQuery = query1 + " union " + query2;
		totQuery = totQuery + " minus " + query3;
		totQuery = totQuery + " union " + query4;
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(totQuery);
		System.out.println("ListSize = " + list.size());
		
		System.out.println("전체걸린 시간 = " + (System.currentTimeMillis() - processStartTime) + "ms");
        
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		
	}
	
}
