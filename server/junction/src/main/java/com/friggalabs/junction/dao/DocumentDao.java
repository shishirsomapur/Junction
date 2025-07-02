package com.friggalabs.junction.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.friggalabs.junction.entity.DocumentEntity;
import com.friggalabs.junction.entity.UserEntity;

@Repository
public interface DocumentDao extends JpaRepository<DocumentEntity, Long> {

	List<DocumentEntity> findByUserEntity(UserEntity userEntity);

}
