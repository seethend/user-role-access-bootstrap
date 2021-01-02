/**
 * 
 */
package com.seeth.utils.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author SeethendReddy
 *
 */
public class AppResponseEntity<T> extends ResponseEntity<T> {
	
	/**
	 * @param body
	 * @param status
	 */
	public AppResponseEntity(T body, HttpStatus status) {
		super(body, status);
	}

	/**
	 * 
	 * 07-Nov-2020 3:15:07 am
	 *
	 *
	 * @param <T>
	 * @param data
	 * @param httpStatus
	 * @return
	 * AppResponseEntity<AppResponseModel<T>>
	 */
	public static <T> AppResponseEntity<AppResponseModel<T>> sendSuccessResponse(T data, HttpStatus httpStatus) {
		return new AppResponseEntity<AppResponseModel<T>>(
				AppResponseModel.forSuccess(data, data.getClass()), httpStatus);
	}

	/**
	 *
	 * 11-Nov-2020 3:15:07 am
	 *
	 *
	 * @param <T>
	 * @param data
	 * @param httpStatus
	 * @return
	 * AppResponseEntity<AppResponseModel<T>>
	 */
	public static <T> AppResponseEntity<AppResponseModel<T>> sendNullSuccessResponse(T data, HttpStatus httpStatus) {
		return new AppResponseEntity<AppResponseModel<T>>(
				AppResponseModel.forSuccess(data, Void.class), httpStatus);
	}


	/**
	 * 
	 * 07-Nov-2020 3:26:42 am
	 *
	 *
	 * @param <T>
	 * @param errorMessage
	 * @param httpStatus
	 * @return
	 * AppResponseEntity<AppResponseModel<T>>
	 */
	public static <T> AppResponseEntity<AppResponseModel<T>> sendErrorResponse(String errorMessage, HttpStatus httpStatus) {
		return new AppResponseEntity<AppResponseModel<T>>(
				AppResponseModel.forError(new AppErrorModel(errorMessage)), httpStatus
			);
	}

	/**
	 * 07-Nov-2020 3:49:20 am
	 * @param data
	 *
	 *
	 * @param errors
	 * @param httpStatus
	 * @return
	 * AppResponseEntity<AppResponseModel<List<UserModel>>>
	 */
	public static <T> AppResponseEntity<AppResponseModel<T>> sendPartialResponse(T data, List<AppErrorModel> errors, HttpStatus httpStatus) {
		return new AppResponseEntity<AppResponseModel<T>>(
				AppResponseModel.forPartialSuccess(data, data.getClass(), errors), httpStatus
			);
	}
}
