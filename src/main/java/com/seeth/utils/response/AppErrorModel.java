package com.seeth.utils.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.seeth.exception.models.AppErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class AppErrorModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppErrorModel.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnore
    private AppErrorCode errorCode;
    @JsonProperty
    private String message;
    @JsonProperty
    private String internalMessage;

    public AppErrorModel(String s) {
        this.message = s;
    }

//    public AppErrorModel(String message) {
//        this.message = message;
//    }

    public AppErrorModel(AppErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getKey();
    }

	@Override
	public String toString() {
		try{
			LOGGER.info("{"+ this.message + "," + this.internalMessage + "}");
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
	}

    
}