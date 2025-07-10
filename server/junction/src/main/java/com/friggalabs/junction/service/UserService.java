package com.friggalabs.junction.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.friggalabs.junction.dao.UserDao;
import com.friggalabs.junction.dto.UserDto;
import com.friggalabs.junction.dto.UserResponseDto;
import com.friggalabs.junction.entity.UserEntity;
import com.friggalabs.junction.exception.ServerErrorException;
import com.friggalabs.junction.exception.UserAlreadyExistsException;
import com.friggalabs.junction.exception.UserNotFoundException;
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

		Optional<UserEntity> userOptional = userDao.findByEmail(userDto.getEmail());

		if (userOptional.isEmpty())
			throw new UserAlreadyExistsException("User already exists.");

		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userEntity.setVerificationToken(UUID.randomUUID().toString());
		userEntity.setEnabled(false);

		UserEntity savedUser = userDao.save(userEntity);

		if (savedUser == null)
			throw new ServerErrorException("Internal server error.");

		emailService.sendVerificationMail(userEntity.getEmail(), userEntity.getVerificationToken());

	}

	public boolean verifyUser(String token) {

		Optional<UserEntity> optionalUser = userDao.findByVerificationToken(token);

		if (optionalUser.isEmpty()) {
			return false;
		}

		UserEntity userEntity = optionalUser.get();
		userEntity.setEnabled(true);
		userEntity.setVerificationToken(null);
		userEntity.setUpdatedAt(LocalDateTime.now());

		userDao.save(userEntity);

		return true;

	}

	public String login(UserLoginRequest userLoginRequest) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword()));
		String token = null;

		if (authentication.isAuthenticated()) {
			token = jwtService.generateToken(userLoginRequest.getEmail());
			Optional<UserEntity> userOptional = userDao.findByEmail(userLoginRequest.getEmail());

			if (userOptional.isEmpty())
				throw new UserNotFoundException("User not found");

			UserEntity verifiedUser = userOptional.get();

			return token + ":" + verifiedUser.getId();

		}

		return "Login Failed";

	}

	public String sendResetLink(String email) {

		Optional<UserEntity> userOptional = userDao.findByEmail(email);

		if (userOptional.isEmpty())
			throw new UserNotFoundException("User not found with that email.");

		UserEntity userEntity = userOptional.get();

		String token = UUID.randomUUID().toString();
		userEntity.setResetToken(token);
		userEntity.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
		userDao.save(userEntity);

		String resetUrl = "http://localhost:5173/reset-password?token=" + token;
		emailService.sendResetMail(email, "Reset your password", "Click the link to reset your password:" + resetUrl);

		return "Check your mail password reset link has been sent";

	}

	public String resetPassword(String token, String newPassword) {
		Optional<UserEntity> optionalUserEntity = userDao.findByResetToken(token);

		if (optionalUserEntity.isEmpty())
			return "invalid token";

		UserEntity userEntity = optionalUserEntity.get();

		if (userEntity.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
			return "Token expired. Please request a new one.";
		}

		String hashedPassword = passwordEncoder.encode(newPassword);
		userEntity.setPassword(hashedPassword);
		userEntity.setResetToken(null);
		userEntity.setResetTokenExpiry(null);
		userDao.save(userEntity);

		return "Password has been reset successfully";
	}

	public List<UserResponseDto> getUsers() {

		Stream<UserResponseDto> users = userDao.findAll().stream()
				.map(userEntity -> new UserResponseDto(userEntity.getEmail()));

		List<UserResponseDto> userEmails = users.collect(Collectors.toList());

		return userEmails;

	}

}
