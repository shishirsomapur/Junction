package com.friggalabs.junction.pojo;

import java.util.Set;

import com.friggalabs.junction.constant.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentUpdateRequest {
    private String title;
    private String content;
    private Visibility visibility;
    private Set<String> sharedWith;
    private boolean isArchieved;
}

