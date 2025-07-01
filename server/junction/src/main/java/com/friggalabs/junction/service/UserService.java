package com.friggalabs.junction.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.friggalabs.junction.dao.UserDao;
import com.friggalabs.junction.dto.UserDto;
import com.friggalabs.junction.entity.UserEntity;
import com.friggalabs.junction.pojo.UserLoginRequest;

@Service
public class UserService {

	private UserDao userDao;

	private ModelMapper mapper;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private EmailService emailService;

	private JwtService jwtService;

	private AuthenticationManager authManager;

	public UserService(UserDao userDao, ModelMapper mapper, EmailService emailService, JwtService jwtService,
			AuthenticationManager authManager) {
		this.userDao = userDao;
		this.mapper = mapper;
		this.emailService = emailService;
		this.authManager = authManager;
		this.jwtService = jwtService;
	}

	public void register(UserDto userDto) {
		UserEntity userEntity = mapper.map(userDto, UserEntity.class);
		System.out.println(userEntity);
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userEntity.setVerificationToken(UUID.randomUUID().toString());
		userEntity.setEnabled(false);
		userDao.save(userEntity);

		emailService.sendVerificationMail(userEntity.getEmail(), userEntity.getVerificationToken());

	}

	public String verifyUser(String token) {
		Optional<UserEntity> optionalUser = userDao.findByVerificationToken(token);

		System.out.println("verifying user");

		if (optionalUser.isEmpty()) {
			return "Invalid Verfication Link";
		}

		UserEntity userEntity = optionalUser.get();
		userEntity.setEnabled(true);
		userEntity.setVerificationToken(null);
		userEntity.setUpdatedAt(LocalDateTime.now());

		userDao.save(userEntity);

		return "Email verified successfully! You can now log in.";
	}

	public String login(UserLoginRequest userLoginRequest) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
		String token = null;

		if (authentication.isAuthenticated()) {
			token = jwtService.generateToken(userLoginRequest.getEmail());
			UserEntity verifiedUser = userDao.findByEmail(userLoginRequest.getEmail());
			return token + ":" + verifiedUser.getId();
		}

		return "Login Failed";

	}

	public String sendResetLink(String email) {
		UserEntity userEntity = userDao.findByEmail(email);

		if (userEntity == null)
			return "User not found with email";

		String token = UUID.randomUUID().toString();
		userEntity.setResetToken(token);
		userEntity.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
		userDao.save(userEntity);

		String resetUrl = "http://localhost:3000/reset-password?token=" + token;
		emailService.sendResetMail(email, "Reset your password", "Click the link to reset:" + resetUrl);

		return "Check your mail password reset link has been sent";
	}

	public String resetPassword(String token, String newPassword) {
		Optional<UserEntity> optionalUserEntity = userDao.findByResetToken(token);
		
		if(optionalUserEntity.isEmpty()) return "invalid token";
		
		UserEntity userEntity = optionalUserEntity.get();
		
		if(userEntity.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			return "Token expired. Please request a new one.";
		}
		
		String hashedPassword = passwordEncoder.encode(newPassword);
		userEntity.setPassword(hashedPassword);
		userEntity.setResetToken(null);
		userEntity.setResetTokenExpiry(null);
		userDao.save(userEntity);
		
		return "Password has been reset successfully";
	}

}
