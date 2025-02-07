package io.github.reinershir.boot.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Util {

	 public static String encode(String input) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-1");
	            byte[] hash = digest.digest(input.getBytes());
	            
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                String hex = Integer.toHexString(0xff & b);
	                if (hex.length() == 1) {
	                    hexString.append('0');
	                }
	                hexString.append(hex);
	            }
	            
	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
}
