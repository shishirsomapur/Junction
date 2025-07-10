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
import com.friggalabs.junction.exception.DocumentNotFoundException;
import com.friggalabs.junction.exception.ServerErrorException;
import com.friggalabs.junction.exception.UserNotFoundException;
import com.friggalabs.junction.pojo.DocumentUpdateRequest;

@Service
public class DocumentService {

	private DocumentDao documentDao;

	private ModelMapper mapper;

	private UserDao userDao;

	public DocumentService(DocumentDao documentDao, ModelMapper mapper, UserDao userDao) {
		this.documentDao = documentDao;
		this.mapper = mapper;
		this.userDao = userDao;
	}

	public String createDocument(DocumentDto documentDto) {

		DocumentEntity documentEntity = mapper.map(documentDto, DocumentEntity.class);

		Optional<UserEntity> userOptional = userDao.findByEmail(documentDto.getUser());

		if (userOptional.isEmpty())
			throw new UserNotFoundException("User Not found with that email.");

		UserEntity userEntity = userOptional.get();

		documentEntity.setUserEntity(userEntity);
		documentEntity.setCreatedAt(LocalDateTime.now());
		documentEntity.setUpdatedAt(LocalDateTime.now());
		DocumentEntity savedDocument = documentDao.save(documentEntity);

		if (savedDocument == null)
			throw new ServerErrorException("Error in creating the document. Please try after sometime.");

		return "Document created successfully";

	}

	public List<DocumentEntity> getDocumentsForUser(String email) {
		
		Optional<UserEntity> userOptional = userDao.findByEmail(email);

		if (userOptional.isEmpty())
			throw new UserNotFoundException("user not found with that email.");
		
		UserEntity userEntity = userOptional.get();

		List<DocumentEntity> documents = documentDao.findByUserEntity(userEntity);

		return documents;
		
	}

	public String updateDocument(Long id, DocumentUpdateRequest documentUpdateRequest) {

		Optional<DocumentEntity> optionalDocument = documentDao.findById(id);

		if (optionalDocument.isEmpty())
			new DocumentNotFoundException("Document not found.");

		DocumentEntity documentEntity = optionalDocument.get();

		documentEntity.setTitle(documentUpdateRequest.getTitle());
		documentEntity.setContent(documentUpdateRequest.getContent());
		documentEntity.setVisibility(documentUpdateRequest.getVisibility());
		documentEntity.setArchieved(documentUpdateRequest.isArchieved());
		documentEntity.setUpdatedAt(LocalDateTime.now());

		if (documentUpdateRequest.getSharedWith() != null) {
			Set<SharedUserEntity> sharedUsers = documentUpdateRequest.getSharedWith().stream()
					.map(email -> new SharedUserEntity(null, email, documentEntity)).collect(Collectors.toSet());
			documentEntity.setSharedWith(sharedUsers);
		}

		DocumentEntity updatedDocument = documentDao.save(documentEntity);
		
		if(updatedDocument == null) throw new ServerErrorException("Failed to update the document. please try after sometime.");
		
		return "document updated successfully.";

	}

}
