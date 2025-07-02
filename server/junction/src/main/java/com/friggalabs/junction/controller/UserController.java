package com.friggalabs.junction.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.friggalabs.junction.dto.UserDto;
import com.friggalabs.junction.pojo.ResetPasswordRequest;
import com.friggalabs.junction.pojo.UserLoginRequest;
import com.friggalabs.junction.pojo.UserRegisterRequest;
import com.friggalabs.junction.service.UserService;

//@CrossOrigin(origins="http://localhost:5173/")
@RestController
@RequestMapping("/users")
public class UserController {
	
	private UserService userService;
	
	private ModelMapper mapper;
	
	public UserController(UserService userService,
			ModelMapper mapper) {
		this.userService = userService;
		this.mapper = mapper;
	}
	
	@PostMapping(path="/register")
	public String register(@RequestBody UserRegisterRequest userRegisterRequest) {
		System.out.println(userRegisterRequest);
		UserDto userDto = mapper.map(userRegisterRequest, UserDto.class);
		System.out.println(userDto);
		userService.register(userDto);
		return "User registered successfully";
	}
	
	@PostMapping(path="/login")
	public String login(@RequestBody UserLoginRequest userLoginRequest) {
		return userService.login(userLoginRequest);
	}
	
	@GetMapping(path="/verify")
	public RedirectView  verifyEmail(@RequestParam String token) {
		boolean isVerified = userService.verifyUser(token);

	    String redirectUrl = isVerified
	        ? "http://localhost:5173?verified=true"
	        : "http://localhost:5173?verified=false";

	    return new RedirectView(redirectUrl);
	}
	
	@GetMapping(path="/")
	public String hello() {
		return "hello";
	}
	
	@PostMapping(path="/forgot-password")
	public String forgotPassword(@RequestParam String email) {
		return userService.sendResetLink(email);
	}
	
	@PostMapping(path="/reset-password")
	public String resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		System.out.println(resetPasswordRequest);
		return userService.resetPassword(resetPasswordRequest.getToken(), resetPasswordRequest.getNewPassword());
	}
	
}
