package com.gus.pattern.factory;

public class Sha512Encryptor extends EncryptorFactory {
	/**
	 * This is a trivial example and obviously the idea of the factory pattern is 
	 * to hide/separate complex construction details from any 'client' code. 
	 */
	@Override
	public EncryptionAlgorithm getEncryptionAlgorithm() {
		return new Sha512EncryptionAlgorithm();   
	}

}
