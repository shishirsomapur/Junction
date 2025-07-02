package com.friggalabs.junction.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.friggalabs.junction.constant.Visibility;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="documents")
public class DocumentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
		
	private String content;
	
	@Enumerated(EnumType.STRING)
	private Visibility visibility = Visibility.PRIVATE;
	
	private boolean isArchieved = false;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private UserEntity userEntity;
	
	@OneToMany(mappedBy="documentEntity", cascade=CascadeType.ALL, orphanRemoval = true)
	private Set<SharedUserEntity> sharedWith = new HashSet<>();
	
}
