package com.friggalabs.junction.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
	
	private String username;
	
	private String password;
	
	private String email;
	
	private String fullName;
	
	private String role;
	
	private boolean enabled = true;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private String resetToken;
	
	private LocalDateTime resetTokenExpiry;
	
}