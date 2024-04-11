package com.admin.dto;

import java.util.List;

import lombok.Data;

@Data
public class wishListRemoveRequest {
	
	private List<Long> wishListIds;

}
