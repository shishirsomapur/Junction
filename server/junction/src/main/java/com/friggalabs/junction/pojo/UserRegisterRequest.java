package com.friggalabs.junction.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisterRequest {
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String fullName;
	
}
