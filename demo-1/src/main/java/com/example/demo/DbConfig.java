package com.example.demo;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DbConfig {
	
	@Bean
	public DataSource datasource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		// oracle
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@127.0.0.1:1521/orcl");
        dataSource.setUsername("hr");
        dataSource.setPassword("123qweQWE");
        
        return dataSource;
	}

	@Bean 
	public JdbcTemplate jdbcTemplate(DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
	
}
