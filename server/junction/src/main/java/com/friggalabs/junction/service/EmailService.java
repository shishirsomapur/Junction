package com.friggalabs.junction.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	private JavaMailSender mailSender;
	
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendVerificationMail(String toEmail, String token) {
		
		String subject = "Verify your email";
		String verificationLink = "http://localhost:8080/users/verify?token=" + token;
		String body = "Hi,\n\nPlease click on the verification link below to verify your email:\n" + verificationLink;
		
		sendMail(subject, body, toEmail);
	
	}
	
	private void sendMail(String subject, String body, String toEmail) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom("shishir9448@gmail.com");
		
		mailSender.send(message);
	}

	public void sendResetMail(String email, String subject, String body) {

		sendMail(subject, body, email);
		
	}
}
