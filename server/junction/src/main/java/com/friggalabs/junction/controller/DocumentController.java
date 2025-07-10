package com.friggalabs.junction.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.friggalabs.junction.util.ResponseStructure;

@RestController
@RequestMapping("/documents")
public class DocumentController {

	private DocumentService documentService;

	private ModelMapper mapper;

	public DocumentController(DocumentService documentService, ModelMapper mapper) {
		this.documentService = documentService;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<ResponseStructure<String>> createDocument(@RequestBody DocumentRequest documentRequest) {

		DocumentDto documentDto = mapper.map(documentRequest, DocumentDto.class);

		String response = documentService.createDocument(documentDto);

		return new ResponseEntity<>(
				new ResponseStructure<>(response, HttpStatus.CREATED.value(), "Document created successfully."),
				HttpStatus.CREATED);

	}

	@GetMapping("/{email}")
	public ResponseEntity<ResponseStructure<List<DocumentEntity>>> getDocumentsForUser(@PathVariable String email) {

		List<DocumentEntity> documents = documentService.getDocumentsForUser(email);

		return new ResponseEntity<>(
				new ResponseStructure<>(documents, HttpStatus.OK.value(), "List of all the documents of the user."),
				HttpStatus.OK);

	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> updateDocument(@PathVariable Long id,
			@RequestBody DocumentUpdateRequest documentUpdateRequest) {

		String response = documentService.updateDocument(id, documentUpdateRequest);

		return new ResponseEntity<>(new ResponseStructure<>(response, HttpStatus.OK.value(), "Document updated."),
				HttpStatus.OK);

	}

	@DeleteMapping("/{id}")
	public void deleteDocument(@PathVariable Long id) {

	}
}
