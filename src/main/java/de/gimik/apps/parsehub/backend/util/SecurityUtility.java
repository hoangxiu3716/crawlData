package de.gimik.apps.parsehub.backend.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.springframework.util.StringUtils;

import com.google.common.io.BaseEncoding;

public class SecurityUtility {
	

	public static String encoderClaimstar(Integer value) {
		if(value == null)
			return "";
		return encoderClaimstar(value.toString());
	}
	public static String encoderClaimstar(String value) {
		if(StringUtils.isEmpty(value))
			return "";
		String result = "" ;
//		result += generateRandomKey(2);
		try {
			result += BaseEncoding.base64().encode(value.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
//		result += generateRandomKey(3);
		return result;
	}
	public static String decoderClaimstar(String value) {
		if(StringUtils.isEmpty(value))
			return "";
//		value = value.substring(2, value.length() -3);
		byte[] contentInBytes = BaseEncoding.base64().decode(value);
		String result;
		try {
			result = new String(contentInBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return result ;
	}
	public static String decoderClaimstar(Integer value) {
		if(value ==null)
			return null;
		String valueStr = value.toString();
		String result = decoderClaimstar(valueStr);
	
			return result;
	
	}
	public static Integer decoderClaimstarIntValue(String value) {
		if(StringUtils.isEmpty(value))
			return null;
		String result = decoderClaimstar(value);
		try {
			return Integer.valueOf(result);
		} catch (Exception e) {
			return null;
		}
	}
	public static String generateRandomKey(int length){
		String alphabet = 
		        new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
		int n = alphabet.length(); //10

		String result = new String(); 
		Random r = new Random(); //11

		for (int i=0; i<length; i++) //12
		    result = result + alphabet.charAt(r.nextInt(n)); //13
		return result;
		}
	public static void main(String[] args) {
		String a = SecurityUtility.decoderClaimstar("NC43MDAwMDE5RTc=");
		System.out.println(a);
//		
//		String result ="";
//		for(int i=0 ;i <98; i++){
//			System.out.println("<div class=\"col\"><img src=\"image-frame/cat_face/cat_face"+i+".png\" ng-click=\"chooseSticker('image-frame/cat_face/cat_face"+i+".png')\"  class=\"fullscreen-image\" /></div>");
//		}
	}
}
