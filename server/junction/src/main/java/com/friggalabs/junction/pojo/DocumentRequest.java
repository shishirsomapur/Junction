package com.friggalabs.junction.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest {
	
	private String title;
	
	private String user;
	
	private String content;
	
}
