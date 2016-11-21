/**
 * 
 */
package com.axway.academy.loren.encryption;

/**
 *
 *
 * @author LorenIvanov
 *
 */
public class GenerateKeyFactory {

	public GenerateKey whichKeyToGenerate(String criteria) {
		if ("RSA".equals(criteria)) {
			return new GenerateKeyRSA();
		} else if ("DSA".equals(criteria)) {
			return new GenerateKeyDSA();
		} else {
			System.out.println("Error generating keys!");
			return null;
		}
	}

}
