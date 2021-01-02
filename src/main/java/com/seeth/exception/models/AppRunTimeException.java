package com.seeth.exception.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Component
public class AppRunTimeException extends RuntimeException{

    private static final long serialVersionUID = -8669902277600073238L;
	
	private AppErrorCode errorCode;
}
