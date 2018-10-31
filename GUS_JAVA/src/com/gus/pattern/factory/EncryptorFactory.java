package com.gus.pattern.factory;

import java.io.FileOutputStream;
import java.io.IOException;
/**
 * <p>An <code>abstract</code> factory class may be overkill but allows for different 
 * concrete factory classes to build different encryption algorithms.
 * <p>A factory may use a Template method or methods to build its "Product":   
 * The <code>Template Method</code> pattern suggests to break down an algorithm into a series of steps, 
 * turn steps into methods and call them one by one inside a single "template" method. 
 * Subclasses will be able to override particular steps, but not the actual template method, 
 * leaving the algorithm structure unchanged.
 * <p>Simplified "Factory" patterns. A "Factory Method" is often a <code>static</code> method 
 * whose sole responsibility is to abstract the creation process of an object and return that object.
 * @author guybe
 *
 */
public abstract class EncryptorFactory {
	/**
	 * Factory helper method to encrypt and write text to a file
	 * @param plaintext
	 * @param filename
	 */
    public void writeToDisk(String plaintext, String filename) {
        EncryptionAlgorithm encryptionAlgorithm = getEncryptionAlgorithm();
        String cyphertext = encryptionAlgorithm.encrypt(plaintext);
        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            outputStream.write(cyphertext.getBytes());
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Concrete 'factory' classes could build and return different 
     * encryption algorithms here. 
     * @return the implementation of {@link EncryptionAlgorithm}
     */
    public abstract EncryptionAlgorithm getEncryptionAlgorithm();

}
