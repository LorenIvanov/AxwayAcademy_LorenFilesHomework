package com.axway.academy.loren;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

public class FileComandLineHomework {

	private Scanner userInput = new Scanner(System.in);
	private String fileContent = null;
	private static String FILE_LOCATION = null;
	private String[] fileValidation = null;
	private Path file = null;
	private Properties replacePairs = new Properties();

	public FileComandLineHomework() {
		int userChoise = 0;
		System.out.println("Please enter file location.");
		FILE_LOCATION = userInput.nextLine();
		file = Paths.get(FILE_LOCATION);
		fileValidation = FILE_LOCATION.split("\\.");
		if (fileValidation[fileValidation.length - 1].equals("txt")) {
			readFile();
			fileContentReplace();
		} else {
			System.out.println("Invalid file extension.");
			System.out.println("Press 1 to enter new file location.");
			userChoise = userIntegerInputCheck();
		}
		if (userChoise == 1) {
			new FileComandLineHomework();
		}
	}

	/**
	 * Replaces the specific content of a file
	 */

	protected void fileContentReplace() {
		Enumeration enumeration;
		int userChoise = 0;
		boolean fileChange = false;
		System.out.println("Please enter how much words you want to replace.");
		userChoise = userIntegerInputCheck();
		if (userChoise > 0) {
			fileChange = true;
		}
		fillPropertyVariable(userChoise);
		enumeration = replacePairs.propertyNames();
		checkContent(enumeration);
		if (fileChange) {
			writeToFile();
			fileChange = false;
		}
	}

	/**
	 * Validates user input
	 */

	private int userIntegerInputCheck() {
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
	 * fileContentReplace's help method to check if file contents the requested
	 * word
	 */

	private void checkContent(Enumeration enumeration) {
		String keyValue;
		for (; enumeration.hasMoreElements();) {
			keyValue = enumeration.nextElement().toString();
			if (fileContent.contains(keyValue)) {
				fileContent = fileContent.replaceAll(keyValue,
						replacePairs.getProperty(keyValue));
			} else {
				System.out
						.println("The file doesn't content the word you entered to replace.");
			}
		}
	}

	/**
	 * fileContentReplace's help method to fill the Property variable
	 */

	private void fillPropertyVariable(int userChoise) {
		String wordToReplace;
		String newWord;
		for (int i = 1; i < userChoise + 1; i++) {
			System.out.println("Please enter the word you want to replace.");
			wordToReplace = userInput.nextLine();
			System.out
					.println("Please enter the new word to replace the old one.");
			newWord = userInput.nextLine();
			replacePairs.put(wordToReplace, newWord);
			if (fileContent.equals(null)) {
				readFile();
			}
		}

	}

	/**
	 * Reads content of a file
	 */

	protected void readFile() {
		try {
			if (Files.exists(file)) {
				byte[] fileBytes = Files.readAllBytes(file);
				fileContent = new String(fileBytes);
				if (fileBytes.length == 0) {
					System.out.println("File is empty.");
				} else {
					System.out.println(fileContent);
				}
			} else {
				System.out.println("File does not exist!");
				Files.createFile(file);
				System.out.println("File is created: " + FILE_LOCATION);
				readFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes to file
	 */

	protected void writeToFile() {
		String newFileName;
		newFileName = newFileName();
		Path newFile = Paths.get(newFileName);
		file = newFile;
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			System.out.println("writing to file..");
			Files.write(file, fileContent.getBytes());
			System.out.println("Successfully written to file.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * writeToFile's help method to generate new file name
	 */

	private String newFileName() {
		String newFileName = "", fileName;
		fileValidation = FILE_LOCATION.split("\\.");
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		fileName = fileValidation[fileValidation.length - 2];
		fileName = fileName.concat("_modified_");
		fileName = fileName.concat(dateFormat.format(date));
		fileName = fileName.concat(".");
		fileValidation[fileValidation.length - 2] = fileName;
		for (int i = 0; i < fileValidation.length; i++) {
			newFileName = newFileName.concat(fileValidation[i]);
		}
		return newFileName;
	}

	public static void main(String[] args) {
		new FileComandLineHomework();
	}
}
