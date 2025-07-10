package com.friggalabs.junction.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friggalabs.junction.entity.UserEntity;

@Repository
public interface UserDao extends JpaRepository<UserEntity, Long>{

	UserEntity findByUsername(String username);

	Optional<UserEntity> findByVerificationToken(String token);

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByResetToken(String token);
	
}
