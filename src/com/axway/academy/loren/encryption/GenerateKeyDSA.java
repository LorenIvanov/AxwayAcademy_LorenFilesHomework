/**
 * 
 */
package com.axway.academy.loren.encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 *
 * @author LorenIvanov
 *
 */
public class GenerateKeyDSA implements GenerateKey {

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
			// create a keypair generator and specify the algorithm with which
			// keys are going to be used.
			// the provide SUN is optional
			KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance("DSA", "SUN");
			// generate the public-private key pair
			KeyPair keyPair = keyGen.generateKeyPair();
			// obtain the private key from the keypair
			privateKey = keyPair.getPrivate();
			// obtain the public key from the keypair
			publicKey = keyPair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Problems during key pair generation.");
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			System.out.println("Problems during key pair generation.");
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
