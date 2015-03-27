package com.gus.pattern.serviceprovider;
/**
 * The UserRegistration interface. 
 * @author Gus
 * @see http://docs.oracle.com/javase/tutorial/ext/basics/spi.html 
 */
public interface UserRegistration {
	
	public boolean canRegister(AbstractUser abstractUser); 
	
	public void registerNewUser() throws UserIdExistsException;
	
	public boolean isUserExists(); 

	public void deleteUser();
}
