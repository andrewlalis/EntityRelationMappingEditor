package nl.andrewlalis.erme.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Hash {
	public static boolean matches(byte[] password, String resourceFile) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		byte[] passwordHash = md.digest(password);
		InputStream is = Hash.class.getClassLoader().getResourceAsStream(resourceFile);
		if (is == null) {
			System.err.println("Could not obtain input stream to admin_hash.txt");
			return false;
		}
		char[] buffer = new char[64];
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			if (br.read(buffer) != buffer.length) {
				System.err.println("Incorrect number of characters read from hash file.");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		String hashHex = String.valueOf(buffer);
		byte[] hash = hexStringToByteArray(hashHex);
		return Arrays.equals(passwordHash, hash);
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}
}
