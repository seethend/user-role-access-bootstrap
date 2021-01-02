package com.seeth.user.utils.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.seeth.exception.models.AppRunTimeException;
import com.seeth.exception.models.AppErrorCode;
import com.seeth.user.personal.models.User;
import com.seeth.user.personal.models.UserAuthDetail;
import com.seeth.user.personal.repositories.UserRepository;

@Component
public class CurrentUserUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserUtil.class);
	
	private static UserRepository userRepository;

    @Autowired
    public CurrentUserUtil(UserRepository userRepository) {
        CurrentUserUtil.userRepository = userRepository;
    }
    
    public static User getLoggedInUser() {

    	try {
    		UserAuthDetail userAuthDetail = 
    				(UserAuthDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		
    		if(userAuthDetail != null) {
	    		Optional<User> user = userRepository.findById(userAuthDetail.getId());
	    		
	    		if(user.isPresent()) {
	    			return user.get();
	    		} else {
	        		LOGGER.error("No User Logged-In");
	        		throw new AppRunTimeException(AppErrorCode.LOGIN_REQUIRED);
	    		}
    		} else {
        		LOGGER.error("No User Logged-In");
        		throw new AppRunTimeException(AppErrorCode.LOGIN_REQUIRED);
    		}
    	} catch(Exception e) {
    		LOGGER.error("No User Logged-In");
    		throw new AppRunTimeException(AppErrorCode.LOGIN_REQUIRED);
    	}
    }
}
