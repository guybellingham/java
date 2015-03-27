package com.gus.pattern.serviceprovider;

import java.util.Date;

public abstract class AbstractUser {
		
	protected String userId;
	protected String password; 
	
	/**
	 * JavaBean no-arg constructor;
	 */
	public AbstractUser() {
		
	}
	/**
	 * constructor - minimal set of fields required to establish a unique user (none of these may be null or empty)
	 * 
	 */
	public AbstractUser(String userId, String password) {
		assert(null!=userId && userId.length()>7);
		assert(null!=password && password.length()>7);
		setUserId(userId);
		setPassword(password);
	}
	/**
	 * Your implementation must override this method:
	 * @return the securely hashed password, not the plaintext password.
	 */
	public abstract String getPassword();    
	/**
	 * Your implementation must override this method:
	 * Should only work if this user {@link #isAuthenticated()}!
	 * @param newPass - the new password for this user
	 */
	protected abstract void setPassword(String newPass); 
	/**
	 * Your implementation must override this method:
	 * @return true if this user successfully authenticated and is 'logged in'.
	 */
	public abstract boolean isAuthenticated();
	/**
	 * Your implementation must override this method:
	 * @return true if this user is disabled and cannot authenticate.
	 */
	public abstract boolean isDisabled();
	
	public abstract int getFailedLoginAttempts();
	
	public abstract Date getLastSuccessfulLogin(); 
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}	
	
	public String toString() {
		return super.toString() + " userId=" + getUserId() + " password=" + getPassword();
	}
	
}
