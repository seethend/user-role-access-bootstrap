package com.seeth.utils.response;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class AppResponseModel<T> {

	@JsonProperty
	private AppResponseStatus status;
	@JsonProperty
    private T data;
	@JsonProperty
    private List<AppErrorModel> errors;
	@JsonIgnore
    private Class<?> dataType;

	public AppResponseModel(AppResponseStatus status, T data, Class<?> dataType, List<AppErrorModel> errors) {
        this.status = status;
        this.data = data;
        this.dataType = dataType;
        this.errors = errors;
	}
	
    public AppResponseModel(AppResponseStatus status, T data, Class<?> dataType) {
        this.status = status;
        this.data = data;
        this.dataType = dataType;
    }

    public AppResponseModel(AppResponseStatus status, List<AppErrorModel> errors) {
        this.status =  status;
        this.errors = errors;
    }

	public static <T> AppResponseModel<T> forSuccess(T data, Class<?> dataType){
        return new AppResponseModel<T>(AppResponseStatus.SUCCESS, data, dataType);
    }
    
    public static <T> AppResponseModel<T> forPartialSuccess(T data, Class<?> dataType, List<AppErrorModel> errors){
    	
    	AppResponseStatus responseStatus = AppResponseStatus.PARTIAL;
    	
    	if(errors == null || errors.size() == 0) {
    		responseStatus = AppResponseStatus.SUCCESS;
    	}
    	
        return new AppResponseModel<T>(responseStatus, data, dataType, errors);
    }

    public static <T> AppResponseModel<T> forError(AppErrorModel... errors){
        return new AppResponseModel<T>(AppResponseStatus.ERROR, Arrays.asList(errors));
    }

    public static <T> AppResponseModel<T> forError(List<AppErrorModel> errors){
        return forError(errors.toArray(new AppErrorModel[errors.size()]));
    }
    
	public static <T> AppResponseModel<T> forError(AppErrorModel cMPSErrorModel) {
		AppErrorModel[] errors = new AppErrorModel[1];
		errors[0] = cMPSErrorModel;
		return forError(errors);
	}

	public AppResponseStatus getStatus() {
		return status;
	}

	public void setStatus(AppResponseStatus status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<AppErrorModel> getErrors() {
		return errors;
	}

	public void setErrors(List<AppErrorModel> errors) {
		this.errors = errors;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public void setDataType(Class<?> dataType) {
		this.dataType = dataType;
	}

	@Override
	public String toString() {
		return "AppResponseModel [status=" + status + ", data=" + data + ", errors=" + errors + ", dataType=" + dataType
				+ "]";
	}
	
}