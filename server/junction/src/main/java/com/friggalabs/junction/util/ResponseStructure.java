package com.friggalabs.junction.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStructure<T> {
	
	private T data;
	private int statusCode;
	private String message;
	
}
