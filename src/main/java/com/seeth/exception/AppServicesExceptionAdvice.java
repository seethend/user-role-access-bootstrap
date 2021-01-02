package com.seeth.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.seeth.exception.models.AppRunTimeException;
import com.seeth.utils.response.AppErrorModel;
import com.seeth.utils.response.AppResponseModel;

@RestControllerAdvice
public class AppServicesExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler( { MethodNotAllowedException.class, AccessDeniedException.class, UsernameNotFoundException.class })
    public AppResponseModel<AppErrorModel> handleMethodNotAllowedException(Exception exception){
        return AppResponseModel.forError(new AppErrorModel("Exception : " + exception.getMessage()));
    }

    @ExceptionHandler(AppRunTimeException.class)
    public AppResponseModel<AppErrorModel> handleCmpsRunTimeException(AppRunTimeException exception){
        return AppResponseModel.forError(new AppErrorModel(exception.getErrorCode()));
    }
    
    @ExceptionHandler( { Exception.class, RuntimeException.class } )
    public AppResponseModel<AppErrorModel> handleOtherException(Exception exception){
        return AppResponseModel.forError(new AppErrorModel("Exception : " + exception.getMessage()));
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public AppResponseModel<AppErrorModel> handleMaxSizeException(MaxUploadSizeExceededException exc) {
      return AppResponseModel.forError(new AppErrorModel("One or more files are too large!"));
    }
}