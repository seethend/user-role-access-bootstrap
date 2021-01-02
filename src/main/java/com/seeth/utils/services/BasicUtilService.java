/**
 * 
 */
package com.seeth.utils.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.seeth.exception.models.AppRunTimeException;
import com.seeth.exception.models.AppErrorCode;

/**
 * @author SeethendReddy
 *
 */
public class BasicUtilService {
	
	private static final String aA0 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	/**
	 * 
	 * 13-Nov-2020 1:44:37 am
	 *
	 *
	 * @return
	 * String
	 */
	public static String generateRandomToken() {
		int leftLimit = 48; // numeral '0'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 20;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		
		System.out.println(generatedString);

		return everyNth(generatedString, 5);
	}

	/**
	 * 
	 * 13-Nov-2020 1:49:10 am
	 *
	 *
	 * @param str
	 * @param n
	 * @return
	 * String
	 */
	public static String everyNth(String str, int n){
		
		if(str == null || str.trim().isEmpty()) {
			throw new AppRunTimeException(AppErrorCode.INVALID_INPUT_DATA);
		}
		
		List<String> timeTokens = getTimeTokens();
		
		StringBuilder sb = new StringBuilder();

		int i = 0;

		Iterator<String> tokenItr = timeTokens.iterator();
		
		for(char c : str.toCharArray()) {
			
			if(i !=0 && i % n == 0 && i != str.length() - 1) {
				if(tokenItr.hasNext()) {
					sb.append("-");
					sb.append(tokenItr.next());
				}
				
				sb.append("-");
			}
			
			sb.append(c);
			i++;
		}
		
		return sb.toString();
	}

	/**
	 * 13-Nov-2020 2:05:07 am
	 *
	 *
	 * @return
	 * Set<String>
	 */
	private static List<String> getTimeTokens() {
		
		List<String> timeTokens = new ArrayList<>();
		
		try {
			String time = System.currentTimeMillis() + "";
			
			int index = 0;
			
			while (index < time.length()) {
				String substring = time.substring(index, Math.min(index + 4,time.length()));
				
				if(substring.length() == 4)
					timeTokens.add(substring);
			    index += 4;
			}
		} catch(Exception e) {
			return new ArrayList<>();
		}
		
		return timeTokens;
	}

	/**
	 * 
	 * 16-Dec-2020 3:27:17 am
	 *
	 *
	 * @param len
	 * @return
	 * String
	 */
	public static String generateRandomPassword(int len) {
		StringBuilder sb = new StringBuilder(len);
		   for(int i = 0; i < len; i++)
		      sb.append(aA0.charAt(rnd.nextInt(aA0.length())));
		   return sb.toString();
	}
}
