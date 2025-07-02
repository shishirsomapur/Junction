package com.friggalabs.junction.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.friggalabs.junction.dto.DocumentDto;
import com.friggalabs.junction.entity.DocumentEntity;
import com.friggalabs.junction.pojo.DocumentRequest;
import com.friggalabs.junction.pojo.DocumentUpdateRequest;
import com.friggalabs.junction.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {
	
	private DocumentService documentService;
	
	private ModelMapper mapper;
	
	public DocumentController(DocumentService documentService,
			ModelMapper mapper) {
		this.documentService = documentService;
		this.mapper = mapper;
	}

	@PostMapping
	public String createDocument(@RequestBody DocumentRequest documentRequest) {
		DocumentDto documentDto = mapper.map(documentRequest, DocumentDto.class);
		return documentService.createDocument(documentDto);
	}
	
	@GetMapping("/{email}")
	public List<DocumentEntity> getDocumentsForUser(@PathVariable String email) {
		List<DocumentEntity> documents = documentService.getDocumentsForUser(email);
		
		return documents;
	}
	
	@PutMapping("/{id}")
	public void updateDocument(@PathVariable Long id, 
			@RequestBody DocumentUpdateRequest documentUpdateRequest) {
		documentService.updateDocument(id, documentUpdateRequest);
	}
	
	@DeleteMapping("/{id}")
	public void deleteDocument(@PathVariable Long id) {
		
	}
}
