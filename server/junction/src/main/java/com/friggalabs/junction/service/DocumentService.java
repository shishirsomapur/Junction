package com.friggalabs.junction.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.friggalabs.junction.dao.DocumentDao;
import com.friggalabs.junction.dao.UserDao;
import com.friggalabs.junction.dto.DocumentDto;
import com.friggalabs.junction.entity.DocumentEntity;
import com.friggalabs.junction.entity.SharedUserEntity;
import com.friggalabs.junction.entity.UserEntity;
import com.friggalabs.junction.pojo.DocumentUpdateRequest;

@Service
public class DocumentService {
	
	private DocumentDao documentDao;
	
	private ModelMapper mapper;
	
	private UserDao userDao;
	
	public DocumentService(DocumentDao documentDao,
			ModelMapper mapper,
			UserDao userDao) {
		this.documentDao = documentDao;
		this.mapper = mapper;
		this.userDao = userDao;
	}

	public String createDocument(DocumentDto documentDto) {
		DocumentEntity documentEntity = mapper.map(documentDto, DocumentEntity.class);
		
		UserEntity userEntity = userDao.findByEmail(documentDto.getUser());
	    if (userEntity == null) {
	        throw new RuntimeException("User not found with email: " + documentDto.getUser());
	    }

	    documentEntity.setUserEntity(userEntity);
	    documentEntity.setCreatedAt(LocalDateTime.now());
	    documentEntity.setUpdatedAt(LocalDateTime.now());
		documentDao.save(documentEntity);
		
		return "Document created successfully";
	}

	public List<DocumentEntity> getDocumentsForUser(String email) {
		UserEntity userEntity = userDao.findByEmail(email);
		
		if(userEntity == null) 
			throw new RuntimeException("user not found");
		
		List<DocumentEntity> documents = documentDao.findByUserEntity(userEntity);
		
		return documents;
	}

	public void updateDocument(Long id, DocumentUpdateRequest documentUpdateRequest) {
		
		Optional<DocumentEntity> optionalDocument = documentDao.findById(id);
		
		if(optionalDocument.isEmpty()) new RuntimeException("Document not found");
		
		DocumentEntity documentEntity = optionalDocument.get();
		
		documentEntity.setTitle(documentUpdateRequest.getTitle());
		documentEntity.setContent(documentUpdateRequest.getContent());
		documentEntity.setVisibility(documentUpdateRequest.getVisibility());
		documentEntity.setArchieved(documentUpdateRequest.isArchieved());
		documentEntity.setUpdatedAt(LocalDateTime.now());
		
		if(documentUpdateRequest.getSharedWith() != null) {
			Set<SharedUserEntity> sharedUsers = documentUpdateRequest.getSharedWith().stream()
					.map(email -> new SharedUserEntity(null, email, documentEntity))
					.collect(Collectors.toSet());
			documentEntity.setSharedWith(sharedUsers);
		}
		
		documentDao.save(documentEntity);
		
	}
	
}
