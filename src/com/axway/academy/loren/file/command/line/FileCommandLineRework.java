package com.axway.academy.loren.file.command.line;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;

/**
 * Extends FileComandLineHomework class and overrides few methods to improve the
 * quality of the code
 *
 * @author LorenIvanov
 *
 */
public class FileCommandLineRework extends FileComandLineHomework {

	private Scanner fileContentValidation = null;
	private Properties stringPairs = new Properties();
	private final static File FILE_STRING_PAIRS = new File("stringPairs.txt");

	public FileCommandLineRework() {
		super();
	}

	/**
	 * Starting to work with file and file location
	 */
	@Override
	protected void execute() {
		int userChoise = 0;
		boolean fileTextContent = false;
		System.out.println("Please enter file location.");
		setFILE_LOCATION(getUserInput().nextLine());
		setFile(Paths.get(getFILE_LOCATION()));
		try {
			fileContentValidation = new Scanner(getFile());
			if (fileContentValidation.hasNextLine()) {
				fileTextContent = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!fileTextContent) {
			System.out.println("Invalid file.");
			System.out.println("Press 1 to enter new file location.");
			userChoise = userIntegerInputCheck();
			if (userChoise == 1) {
				execute();
			}
		}
		readFile(getFILE_LOCATION());
		fileContentReplace();
	}

	/**
	 * Replaces the specific content of a file and works with properties
	 */
	@Override
	protected void fileContentReplace() {
		int userChoise = 0;
		boolean fileChange = false;
		fillProperties();
		while (true) {
			System.out.println("Press 1 to see previous string pairs.");
			System.out.println("Press 2 to choose from previous string pairs.");
			System.out.println("Press 3 to enter new string pairs.");
			System.out.println("Press 4 to remove old string pairs.");
			System.out.println("Press 0 to exit cycle.");
			userChoise = userIntegerInputCheck();
			if (userChoise == 1) {
				readFile("stringPairs.txt");
			}
			if (userChoise == 2) {
				int lineCounter = 0;
				readFile("stringPairs.txt");
				System.out.println("Choose line.");
				userChoise = 0;
				userChoise = userIntegerInputCheck();
				try {
					fileContentValidation = new Scanner(FILE_STRING_PAIRS);
				} catch (FileNotFoundException e) {
					System.out.println("File not found!");
					e.printStackTrace();
				}
				while (fileContentValidation.hasNextLine()) {
					lineCounter++;
					if (userChoise == lineCounter) {
						fileContentReplace(userChoise, true);
						break;
					}
				}
				fileChange = true;
			}
			if (userChoise == 3) {
				userChoise = 0;
				System.out
						.println("Please enter how much pairs you want to enter.");
				userChoise = userIntegerInputCheck();
				fillPropertyVariable(userChoise);
				writeProperties();
			}
			if (userChoise == 4) {
				userChoise = 0;
				readFile("stringPairs.txt");
				System.out.println("Please choose line to remove.");
				userChoise = userIntegerInputCheck();
				fileContentReplace(userChoise, false);
				writeProperties();
			}
			if (userChoise == 0) {
				break;
			}
			if (fileChange) {
				writeToFile();
				fileChange = false;
			}
			userChoise = 0;
			fileChange = false;
		}
	}

	/**
	 * Puts string pairs into properties class
	 * 
	 * @param userChoise
	 *            - number of pairs to enter
	 */
	@Override
	protected void fillPropertyVariable(int userChoise) {
		String wordToReplace;
		String newWord;
		for (int i = 1; i < userChoise + 1; i++) {
			System.out.println("Please enter the word you want to replace.");
			wordToReplace = getUserInput().nextLine();
			System.out
					.println("Please enter the new word to replace the old one.");
			newWord = getUserInput().nextLine();
			stringPairs.put(wordToReplace, newWord);
			if (getFileContent().equals(null)) {
				readFile();
			}
		}
	}

	/**
	 * Writes properties to file
	 */
	protected void writeProperties() {
		Path file = Paths.get("stringPairs.txt");
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
			String stringPairs = getPropertiesToString();
			Files.write(file, stringPairs.getBytes());
			System.out.println("Successfully written to file.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates string from properties variable
	 * 
	 * @return properties to string ready to be written to file
	 */
	protected String getPropertiesToString() {
		String propertiesGenereted = "";
		Enumeration e = stringPairs.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			propertiesGenereted = propertiesGenereted.concat(key);
			propertiesGenereted = propertiesGenereted.concat(" ");
			propertiesGenereted = propertiesGenereted.concat(stringPairs
					.getProperty(key));
			propertiesGenereted = propertiesGenereted.concat("\r\n");
		}
		return propertiesGenereted;
	}

	/**
	 * Replaces the specific content of a file
	 * 
	 * @param userChoise
	 *            user chooses line to remove/replace from properties
	 * @param condition
	 *            if true replace else removes
	 */
	protected void fileContentReplace(int userChoise, boolean condition) {
		Enumeration e = stringPairs.propertyNames();
		int propCounter = 0;
		while (e.hasMoreElements()) {
			propCounter++;
			String key = (String) e.nextElement();
			if (propCounter == userChoise && condition) {
				if (getFileContent().contains(key)) {
					setFileContent(getFileContent().replaceAll(key,
							stringPairs.getProperty(key)));
				} else {
					System.out
							.println("The file doesn't content the word you entered to replace.");
				}
			} else if (userChoise == propCounter && !condition) {
				stringPairs.remove(key);
			}
		}

	}

	/**
	 * Loads properties from file
	 */
	protected void fillProperties() {
		try {
			fileContentValidation = new Scanner(FILE_STRING_PAIRS);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line;
		String[] linePairs = new String[2];
		while (fileContentValidation.hasNextLine()) {
			line = fileContentValidation.nextLine();
			linePairs = line.split(" ");
			stringPairs.put(linePairs[0], linePairs[1]);
		}
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
			if (getFILE_LOCATION() == fileLocation) {
				setFileContent("");
			}
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				if (getFILE_LOCATION() == fileLocation) {
					setFileContent(getFileContent().concat(line).concat("\r\n"));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crates object from FileCommandLineRework class and calls its method
	 * execute()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Factory write and read
		FileCommandLineRework obj = new FileCommandLineRework();
		obj.execute();
	}

}
