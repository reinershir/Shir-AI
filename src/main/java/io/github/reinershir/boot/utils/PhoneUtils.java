package io.github.reinershir.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class PhoneUtils {

	private static Logger logger = LoggerFactory.getLogger(PhoneUtils.class);

	public static boolean isValidPhoneNumber(String phoneNumber) {
		if ((phoneNumber != null) && (!phoneNumber.isEmpty())) {
			return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
		}
		return false;
	}
}
