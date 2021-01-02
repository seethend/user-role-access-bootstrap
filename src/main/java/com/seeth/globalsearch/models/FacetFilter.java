package com.seeth.globalsearch.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonSerialize
public class FacetFilter {

	@JsonProperty
    private long id;

	@JsonProperty
    private String name;

	@JsonProperty
    private Object value;

	@JsonProperty
    private Object max;

	@JsonProperty
    private Object min;

	@JsonProperty
    private boolean enabled;

	@JsonProperty
    private OperationType operationType;

}
