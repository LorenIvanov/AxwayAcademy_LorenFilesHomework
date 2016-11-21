/**
 * 
 */
package com.axway.academy.loren.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Interface to describe how different objects should look like
 *
 * @author LorenIvanov
 *
 */
public interface GenerateKey {
	public void generateKeys();

	/**
	 * @return
	 */
	public PublicKey getPublicKey();

	/**
	 * @return
	 */
	public PrivateKey getPrivateKey();
}
