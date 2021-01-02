package com.seeth.globalsearch.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Data
@AllArgsConstructor
@JsonSerialize
public class GlobalSearchModel {

	@JsonProperty
    private HashMap<String, List<FacetFilter>> facets;
    
	@JsonProperty
	private long count;
	
	@JsonProperty
    private Object results;
	
	@JsonProperty
	private List<String> exportColumns;
	
	@JsonProperty
    private List<FacetFilter> sortBy;
}
