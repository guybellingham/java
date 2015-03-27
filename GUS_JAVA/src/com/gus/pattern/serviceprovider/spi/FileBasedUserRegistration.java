package com.gus.pattern.serviceprovider.spi;
import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gus.pattern.serviceprovider.AbstractUser;
import com.gus.pattern.serviceprovider.UserIdExistsException;
import com.gus.pattern.serviceprovider.UserRegistration;

public class FileBasedUserRegistration implements UserRegistration {

	private static final Logger logger = LogManager.getLogger(AbstractUser.class);
	
	private static final String SUFFIX = ".xml";
	
	private AbstractUser abstractUser; 
	
	/**
	 * Must have a no-arg constructor.
	 */
	public FileBasedUserRegistration() {
		
	}
	
	@Override
	public boolean canRegister(AbstractUser abstractUser) {
		setAbstractUser(abstractUser);
		return true;    //all types of User
	}
	
	@Override
	public void registerNewUser() throws UserIdExistsException {
		if(isUserExists()) {
			throw new UserIdExistsException("A user with the id \""+abstractUser.getUserId()+"\" already exists!");
		}
		// save user details to their profile
		setupProfile();
	}

	@Override
	public boolean isUserExists() {
		Path path = Paths.get("C:\\temp\\", abstractUser.getUserId()+SUFFIX);
		return Files.exists(path, LinkOption.NOFOLLOW_LINKS);
	}
	
	@Override
	public void deleteUser() {
		String userId = abstractUser.getUserId();
		try {
			Path path = Paths.get("C:\\temp\\", userId+SUFFIX);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.error("FAILED to delete profile "+userId+SUFFIX,e); 
		}
	}
	
	private void setupProfile() {
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		XMLEncoder encoder = null;
		try {
			Path path = Paths.get("C:\\temp\\", abstractUser.getUserId()+SUFFIX);
			fos = new FileOutputStream(path.toFile());
			bos = new BufferedOutputStream(fos);
			encoder = new XMLEncoder(bos);
			encoder.setExceptionListener(new ExceptionListener() {
			    public void exceptionThrown(Exception exception) {
			        exception.printStackTrace();
			    }
			});
			encoder.writeObject(abstractUser);
		} catch (Exception e) {
			logger.error("FAILED to write profile "+abstractUser.getUserId()+SUFFIX, e); 
		} finally {
			if(null!=encoder) {
				encoder.close();
			}
			if(null!=bos) {
				try {
					bos.close();
				} catch (IOException e) {
					//ignore
				}
			}
		}
	}

	public AbstractUser getAbstractUser() {
		return abstractUser;
	}

	private void setAbstractUser(AbstractUser abstractUser) {
		this.abstractUser = abstractUser;
	}

}
