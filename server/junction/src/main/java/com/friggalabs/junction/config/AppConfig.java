package com.friggalabs.junction.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {
	
	@Bean
	ModelMapper modelMapper() {
		
		 ModelMapper mapper = new ModelMapper();
		 
		 mapper.getConfiguration()
		 .setSkipNullEnabled(true)
		 .setFieldMatchingEnabled(true)
		 .setFieldAccessLevel(AccessLevel.PRIVATE);
		 
		return mapper;
		
	}
	
}
