/**
 * 
 */
package com.axway.academy.loren.encryption;

/**
 * Class which stores the location of the file which stores the encrypted key
 *
 * @author LorenIvanov
 *
 */
public class KeyStoreLocationSingleton {

	private static KeyStoreLocationSingleton object = null;

	/**
	 * @return the object
	 */
	public static KeyStoreLocationSingleton getInstance() {
		// a check if an instance already exists
		// such construction is useful because a new object is not created until
		// requested to
		if (object == null) {
			object = new KeyStoreLocationSingleton();
		}
		return object;
	}

	private KeyStoreLocationSingleton() {
	}

	/**
	 * 
	 * @return file location
	 */
	public String getFileLocation() {
		return "keyStoringFile.txt";
	}

	public String getPrivateKeyFileLocation() {
		return "privateKeyStoreFile.txt";
	}
}
