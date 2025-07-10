package com.friggalabs.junction.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.friggalabs.junction.dto.UserDto;
import com.friggalabs.junction.dto.UserResponseDto;
import com.friggalabs.junction.pojo.ResetPasswordRequest;
import com.friggalabs.junction.pojo.UserLoginRequest;
import com.friggalabs.junction.pojo.UserRegisterRequest;
import com.friggalabs.junction.service.UserService;
import com.friggalabs.junction.util.ResponseStructure;

//@CrossOrigin(origins="http://localhost:5173/")
@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	private ModelMapper mapper;

	public UserController(UserService userService, ModelMapper mapper) {
		this.userService = userService;
		this.mapper = mapper;
	}

	@PostMapping(path = "/register")
	public ResponseEntity<ResponseStructure<String>> register(@RequestBody UserRegisterRequest userRegisterRequest) {

		UserDto userDto = mapper.map(userRegisterRequest, UserDto.class);
		userService.register(userDto);

		return new ResponseEntity<>(new ResponseStructure<>("User registered successfully.", HttpStatus.CREATED.value(),
				"User created successfully."), HttpStatus.CREATED);

	}

	@PostMapping(path = "/login")
	public ResponseEntity<ResponseStructure<String>> login(@RequestBody UserLoginRequest userLoginRequest) {

		String response = userService.login(userLoginRequest);

		return new ResponseEntity<>(
				new ResponseStructure<>(response, HttpStatus.OK.value(), "User logged in successfully."),
				HttpStatus.OK);

	}

	@GetMapping(path = "/verify")
	public RedirectView verifyEmail(@RequestParam String token) {
		boolean isVerified = userService.verifyUser(token);

		String redirectUrl = isVerified ? "http://localhost:5173?verified=true"
				: "http://localhost:5173?verified=false";

		return new RedirectView(redirectUrl);
	}

	@PostMapping(path = "/forgot-password")
	public ResponseEntity<ResponseStructure<String>> forgotPassword(@RequestParam String email) {
		
		String response = userService.sendResetLink(email);
		return new ResponseEntity<>(new ResponseStructure<>(response, HttpStatus.OK.value(), "Please check you email."),
				HttpStatus.OK);
		
	}

	@PostMapping(path = "/reset-password")
	public ResponseEntity<ResponseStructure<String>> resetPassword(
			@RequestBody ResetPasswordRequest resetPasswordRequest) {

		String response = userService.resetPassword(resetPasswordRequest.getToken(),
				resetPasswordRequest.getNewPassword());

		return new ResponseEntity<>(
				new ResponseStructure<>(response, HttpStatus.OK.value(), "Please check your email."), HttpStatus.OK);

	}

	@GetMapping(path = "/")
	public ResponseEntity<List<UserResponseDto>> getUsers() {

		List<UserResponseDto> users = userService.getUsers();
		return new ResponseEntity<>(users, HttpStatus.OK);

	}

}
