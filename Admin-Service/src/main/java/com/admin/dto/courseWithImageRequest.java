package com.admin.dto;

import lombok.Data;

@Data
public class courseWithImageRequest {
	
	private courseRequest courseRequest;
	
	private ImageRequest imageRequest;

}
