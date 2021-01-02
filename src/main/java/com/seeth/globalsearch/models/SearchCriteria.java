package com.seeth.globalsearch.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria  {
	private String entity;
    private String key;
    private OperationType operationType;
    private Object value;
    private Object maxVal;

    public SearchCriteria(String key, OperationType operationType, Object value) {
        this.key = key;
        this.operationType = operationType;
        this.value = value;
    }
    
    public SearchCriteria(String entity, String key, OperationType operationType, Object value) {
    	this.entity = entity;
        this.key = key;
        this.operationType = operationType;
        this.value = value;
    }
}
