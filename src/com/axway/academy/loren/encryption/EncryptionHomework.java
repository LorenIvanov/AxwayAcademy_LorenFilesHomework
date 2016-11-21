package com.axway.academy.loren.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class for encryption and decryption
 *
 * @author LorenIvanov
 *
 */
public class EncryptionHomework {

	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	private static int userChoise;
	private Scanner userInput = new Scanner(System.in);
	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;
	private KeyStoreLocationSingleton keyStoreLocation = KeyStoreLocationSingleton
			.getInstance();

	/**
	 * Initialization vector value - initial value to be used for encryption of
	 * the first block of data.
	 */
	public static String IV = "AAAAAAAAAAAAAAAA";

	/**
	 * Menu for user
	 */
	protected void menu() {
		System.out.println("Press 1 for encryption.");
		System.out.println("Press 2 for dencryption.");
		System.out.println("Press 0 to exit menu.");
		userChoise = userIntegerInputCheck();
		switch (userChoise) {
		case 0:
			System.out.println("Have a nice day!");
			return;
		case 1:
			encryption();
			break;
		case 2:
			dencryption();
			break;
		default:
			System.out.println("Please select option from menu.");
			break;
		}
		menu();
	}

	/**
	 * Main method for encrypting
	 */
	private void encryption() {
		String randomStringKey = randomString(16);
		System.out.println("Enter file location.");
		String fileLocation = userInput.nextLine();
		Scanner readingFile = null;
		String fileContent = "";
		try {
			readingFile = new Scanner(new File(fileLocation));
			while (readingFile.hasNextLine()) {
				fileContent = fileContent.concat(readingFile.nextLine());
				fileContent = fileContent.concat("\r\n");
			}
			fileContent = padTextToBeMultipleTo16(fileContent);
			byte[] encryptedFileContent = encrypt(fileContent, randomStringKey);
			writeToFile(encryptedFileContent, fileLocation);
			encryptingKey(randomStringKey);
			encryptionOutput(fileLocation);
		} catch (FileNotFoundException e) {
			System.out.println("Problem with file location.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem with encryptiong.");
			e.printStackTrace();
		}

	}

	/**
	 * @param fileLocation
	 *            - where is the file on the system
	 * 
	 */
	private void encryptionOutput(String fileLocation) {
		System.out.println("Encrypted file content:");
		readFile(fileLocation);
		System.out.println("Encrypted key:");
		readFile(keyStoreLocation.getFileLocation());
	}

	/**
	 * Reads file and display its content
	 * 
	 * @param fileLocation
	 *            - the location of the file
	 */
	protected void readFile(String fileLocation) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(fileLocation));
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param randomStringKey
	 *            - key for encrypting
	 * @throws Exception
	 */
	private void encryptingKey(String randomStringKey) throws Exception {
		GenerateKeyFactory gkf = new GenerateKeyFactory();
		GenerateKey gk = gkf.whichKeyToGenerate("RSA");
		gk.generateKeys();
		publicKey = gk.getPublicKey();
		privateKey = gk.getPrivateKey();
		byte[] encryptedText = encrypt(randomStringKey, publicKey);
		writeToFile(encryptedText, keyStoreLocation.getFileLocation());
		writeToFile(privateKey.getEncoded(),
				keyStoreLocation.getPrivateKeyFileLocation());
	}

	/**
	 * Writes to file
	 * 
	 * @param encryptedFileContent
	 *            - content to be written
	 * @param fileLocation
	 *            - location of file
	 */
	private void writeToFile(byte[] encryptedFileContent, String fileLocation) {
		Path file = Paths.get(fileLocation);
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
			System.out.println("writing to file..");
			Files.write(file, encryptedFileContent);
			System.out.println("Successfully written to file.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Encrypts some data using a symmetric key.
	 * 
	 * @param text
	 *            - text to be encrypted
	 * @param encryptionKey
	 *            - key used for encryption
	 * @return the encrypted data
	 * @throws Exception
	 */
	protected byte[] encrypt(String text, String encryptionKey)
			throws Exception {
		// create an instance of the Cipher
		// specify the algorithm sued for encryption
		// the provider SunJCE is optional
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

		// transform the key in the needed format by specifying the algorithm
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
				"AES");

		// initialize the cipher
		// not that an initialization vector is also provided - it is also
		// optional
		cipher.init(Cipher.ENCRYPT_MODE, key,
				new IvParameterSpec(IV.getBytes("UTF-8")));

		// perform encryption
		return cipher.doFinal(text.getBytes("UTF-8"));
	}

	/**
	 * Performs asymmetric encryption.
	 * 
	 * @param text
	 *            - text to be encrypted
	 * @param encryptionKey
	 *            - key sued for encryption
	 * @return the encrypted content
	 * @throws Exception
	 */
	public static byte[] encrypt(String text, PublicKey encryptionKey)
			throws Exception {
		// create the cipher with the correct asymmetric algorithm used
		Cipher cipher = Cipher
				.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		// initialize the cipher by providing the encryption key and encryption
		// mode
		// no initialization vector used this time as it provides no sensible
		// value
		cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);

		// perform encryption
		return cipher.doFinal(text.getBytes("UTF-8"));
	}

	/**
	 * Generates random string
	 * 
	 * @param len
	 *            - size of string
	 * @return generated string
	 */
	String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	/**
	 * Performs text padding with white spaces to be multiple of 16 bytes.
	 * 
	 * @param text
	 *            - text to be padded
	 * @return the padded text
	 */
	public static String padTextToBeMultipleTo16(String text) {
		int textSize = text.getBytes().length;
		int leftover = textSize % 16;
		if (leftover > 0) {
			for (int i = 0; i < 16 - leftover; i++) {
				text = text + " ";
			}
		}
		return text;
	}

	/**
	 * Main method for decrypting
	 */
	private void dencryption() {
		System.out.println("Enter file location.");
		String fileLocation = userInput.nextLine();
		String key = decryptingKey();
		Path path = Paths.get(fileLocation);
		byte[] encryptedContent;
		try {
			encryptedContent = Files.readAllBytes(path);
			String fileContent = decrypt(encryptedContent, key);
			writeToFile(fileContent.getBytes(), fileLocation);
		} catch (Exception e) {
			System.out.println("Problem with decrypting.");
			e.printStackTrace();
		}

	}

	/**
	 * Decrypts a cipher text with a symmetric key.
	 * 
	 * @param cipherText
	 *            - the encrypted content
	 * @param encryptionKey
	 *            - key used for decryption
	 * @return the decrypted content
	 * @throws Exception
	 */
	public static String decrypt(byte[] cipherText, String encryptionKey)
			throws Exception {
		// create an instance of the Cipher
		// specify the algorithm sued for encryption
		// the provider SunJCE is optional
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");

		// transform the key in the needed format by specifying the algorithm
		SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"),
				"AES");

		// initialize the cipher
		// not that an initialization vector is also provided - it is also
		// optional
		cipher.init(Cipher.DECRYPT_MODE, key,
				new IvParameterSpec(IV.getBytes("UTF-8")));

		// perform decryption
		return new String(cipher.doFinal(cipherText), "UTF-8");
	}

	/**
	 * Performs asymmetric decryption.
	 * 
	 * @param encryptedContent
	 *            - encrypted content
	 * @param encryptionKey
	 *            - key used for decryption
	 * @return the decrypted text
	 * @throws Exception
	 */
	public static String decrypt(byte[] encryptedContent,
			PrivateKey encryptionKey) throws Exception {
		// create the cipher with the correct asymmetric algorithm used
		Cipher cipher = Cipher
				.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		// initialize the cipher by providing the encryption key and encryption
		// mode
		// no initialization vector used this time as it provides no sensible
		// value
		cipher.init(Cipher.DECRYPT_MODE, encryptionKey);

		// perform decryption
		return new String(cipher.doFinal(encryptedContent), "UTF-8");
	}

	/**
	 * @return - decrypted key
	 */
	private String decryptingKey() {
		Path path = null;
		byte[] byteArray;
		String key = null;
		try {
			path = Paths.get(keyStoreLocation.getPrivateKeyFileLocation());
			byteArray = Files.readAllBytes(path);
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(
					new PKCS8EncodedKeySpec(byteArray));
			path = Paths.get(keyStoreLocation.getFileLocation());
			byteArray = Files.readAllBytes(path);
			key = decrypt(byteArray, privateKey);
		} catch (IOException e) {
			System.out.println("Problem when decrypting key!");
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			System.out.println("Problem when decrypting key!");
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Problem when decrypting key!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Problem when decrypting key!");
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * Validates user input
	 * 
	 * @return the userChoise
	 */
	protected int userIntegerInputCheck() {
		int userChoise = 0;
		while (!userInput.hasNextInt()) {
			System.out.println("Please enter valid number.");
			userInput.next();
		}
		userChoise = userInput.nextInt();
		userInput.nextLine();
		return userChoise;
	}

	/**
	 * Creates EncryptionHomework object and invokes its method menu()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EncryptionHomework obj = new EncryptionHomework();
		obj.menu();
	}

}
