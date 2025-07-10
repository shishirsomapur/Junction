package com.friggalabs.junction.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.friggalabs.junction.dao.UserDao;
import com.friggalabs.junction.entity.UserEntity;
import com.friggalabs.junction.exception.UserNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	UserDao userDao;

	public CustomUserDetailsService(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Optional<UserEntity> userOptional = userDao.findByEmail(email);
		
		if(userOptional.isEmpty()) throw new UserNotFoundException("User not found with that email.");
		
		UserEntity userEntity = userOptional.get();
		
		System.out.println(userEntity);

		if (userEntity == null)
			new UsernameNotFoundException("User not found with email: " + email);

		return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(),
				userEntity.isEnabled(), 
				true, true, true, List.of(new SimpleGrantedAuthority("USER"))
		);
	}

}
