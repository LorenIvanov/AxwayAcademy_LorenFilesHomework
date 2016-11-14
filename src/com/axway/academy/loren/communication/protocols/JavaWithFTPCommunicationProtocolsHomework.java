package com.axway.academy.loren.communication.protocols;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * CRUD operations using FTP connection to server
 *
 * @author LorenIvanov
 *
 */
public class JavaWithFTPCommunicationProtocolsHomework {

	private static Scanner USER_INPUT = new Scanner(System.in);
	private FTPClient client = null;

	/**
	 * Initialization credentials
	 */
	private static String SERVER = null; // "localhost";
	private static String USERNAME = null; // "LorenServer";
	private static String PASSWORD = null; // "123";
	private static int PORT;

	/**
	 * Class constructor initializing server address, username and password
	 */

	public JavaWithFTPCommunicationProtocolsHomework() {
		System.out.print("Please enter server address: ");
		SERVER = USER_INPUT.nextLine();
		System.out.print("Please enter username: ");
		USERNAME = USER_INPUT.nextLine();
		System.out.print("Please enter password: ");
		PASSWORD = USER_INPUT.nextLine();
		System.out.print("Please enter port: ");
		PORT = userIntegerInputCheck();
	}

	/**
	 * CRUD menu for user
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	protected void menuCRUD() throws IOException, Exception {
		boolean menuCycle = true;
		System.out.println("This is CRUD menu. Please select an option.");
		System.out.println("1. List files inside the directory.");
		System.out.println("2. Download file.");
		System.out.println("3. Upload file.");
		System.out.println("4. Delete file.");
		System.out.println("0. Exit.");
		int userChoise = 0;
		userChoise = userIntegerInputCheck();
		switch (userChoise) {
		case 1:
			listFiles();
			break;
		case 2:
			downloadFile();
			break;
		case 3:
			uploadFile();
			break;
		case 4:
			deleteFile();
			break;
		case 0:
			menuCycle = false;
			break;
		default:
			menuCRUD();
			break;
		}
		if (menuCycle) {
			menuCRUD();
		}
	}

	/**
	 * Validates user input
	 * 
	 * @return validated integer
	 */
	private int userIntegerInputCheck() {
		while (!USER_INPUT.hasNextInt()) {
			System.out.println("Please enter valid number.");
			USER_INPUT.next();
		}
		int userChoise = USER_INPUT.nextInt();
		USER_INPUT.nextLine();
		return userChoise;
	}

	/**
	 * Deleting file from server
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	private void deleteFile() throws IOException, Exception {
		System.out.println("Choose file to delete:");
		listFiles();
		String fileToDelete = USER_INPUT.nextLine();
		boolean success = client.deleteFile(fileToDelete);
		System.out.println(success ? "Succeed to delete " + fileToDelete
				: "Deleting failed");
	}

	/**
	 * User uploads file to server
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	private void uploadFile() throws IOException, Exception {
		System.out
				.println("Enter the location of the file you want to upload.");
		String fileName = USER_INPUT.nextLine();
		InputStream in = new FileInputStream(fileName);
		int beginIndex = fileName.lastIndexOf('\\');
		fileName = fileName.substring(beginIndex + 1, fileName.length());
		boolean success = client.storeFile(fileName, in);
		// boolean success = client.appendFile(fileName, in);
		System.out.println(success ? "Succeed to upload " + fileName
				: "Uploading failed");
		if (in != null) {
			in.close();
		}
	}

	/**
	 * Downloads file
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	private void downloadFile() throws IOException, Exception {
		System.out.println("Enter the name of the file you want to download.");
		String fileName = USER_INPUT.nextLine();
		OutputStream out = new FileOutputStream(new File(fileName));
		boolean success = client.retrieveFile(fileName, out);
		System.out.println(success ? "Succeed to download " + fileName
				: "Downloading failed");
		if (out != null) {
			out.close();
		}
	}

	/**
	 * Lists all files on the server
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	private void listFiles() throws IOException, Exception {
		FTPFile[] files = null;
		files = client.listFiles();
		String[] fileNames = client.listNames();
		if (files != null && fileNames.length > 0) {
			for (String aFile : fileNames) {
				System.out.println(aFile);
			}
		}
	}

	/**
	 * Creates FTP connection to the server (localhost)
	 *
	 * @throws IOException
	 * @throws Exception
	 */
	private void ftpConnection() throws IOException, Exception {
		client = new FTPClient();
		client.connect(SERVER, PORT);

		// connect to the server and authenticate
		client.connect(SERVER);
		client.login(USERNAME, PASSWORD);
	}

	/**
	 * Core method of JavaWithFTPCommunicationProtocolsHomework class which
	 * calls JavaWithFTPCommunicationProtocolsHomework methods
	 */
	protected void execute() {
		try {
			ftpConnection();
			menuCRUD();
		} catch (IOException e) {
			System.out.println("Handeled IOException!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Handeled Exception!");
			e.printStackTrace();
		} finally {
			if (client != null && client.isConnected()) {
				try {
					client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Creating an instance of JavaWithFTPCommunicationProtocolsHomework class
	 * and working with its methods
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		JavaWithFTPCommunicationProtocolsHomework obj = new JavaWithFTPCommunicationProtocolsHomework();
		obj.execute();

	}
}
