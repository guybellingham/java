package com.gus.pattern.factory;
/**
 * This is the interface of the factory "Product".
 * @author guybe
 * @see Sha256EncryptionAlgorithm
 */
public interface EncryptionAlgorithm {
	/**
	 * All Algorithms provide the ability to return the encrypted version of a String.
	 * @param plaintext <-- input
	 * @return the encrypted version of <code>plaintext</code>
	 */
    public String encrypt(String plaintext);
}
