/**
 * 
 */
package com.axway.academy.loren.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 *
 * @author LorenIvanov
 *
 */
public class GenerateKeyRSA implements GenerateKey {

	private PrivateKey privateKey = null;
	private PublicKey publicKey = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.axway.academy.loren.encryption.GenerateKey#generateKeys()
	 */
	@Override
	public void generateKeys() {
		try {
			// Key generation logic START
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			KeyPair keyPair = keyGen.generateKeyPair();

			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
			// Key generation logic END

		} catch (Exception e) {
			System.out.println("Eror during encryption/decryption.");
			e.printStackTrace();
		}

	}

	/**
	 * @return the privateKey
	 */
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	/**
	 * @return the publicKey
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

}
