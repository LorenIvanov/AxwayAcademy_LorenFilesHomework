package com.axway.academy.loren.file.command.line;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Extends FileComandLineHomework class and overrides few methods to improve the
 * quality of the code
 *
 * @author LorenIvanov
 *
 */
public class FileCommandLineRework extends FileComandLineHomework {

	Scanner fileContentValidation = null;

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

	@Override
	protected void fileContentReplace() {
		int userChoise = 0;
		boolean fileChange = false;
		while (true) {
			System.out.println("Press 1 to see previous string pairs.");
			System.out.println("Press 2 to choose from previous string pairs.");
			System.out.println("Press 3 to enter new string pairs.");
			System.out.println("Press 0 to exit cicle.");
			userChoise = userIntegerInputCheck();
			if (userChoise == 1) {
				readFile("stringPairs.txt");
			}
			if (userChoise == 2) {
				//
				readFile("stringPairs.txt");
				fileChange=true;
			}
			if (userChoise == 3) {
				//
				userChoise = 0;
				System.out
						.println("Please enter how much words you want to replace.");
				userChoise = userIntegerInputCheck();
					fileChange = true;
			}
			if (userChoise == 0) {
				break;
			}
			if (fileChange) {
				writeToFile();
				fileChange = false;
			}
			fileChange=false;
		}
		/*
		 * fillPropertyVariable(userChoise); enumeration =
		 * replacePairs.propertyNames(); checkContent(enumeration); if
		 * (fileChange) { writeToFile(); fileChange = false; }
		 */
	}

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
	 * @param args
	 */
	public static void main(String[] args) {
		// D:\Programming\Java\Eclipse\LunaProjects\AxwayAcademy\documentTest.docx
		FileCommandLineRework obj = new FileCommandLineRework();
		obj.execute();
	}

}
