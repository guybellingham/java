package com.gus.pattern.serviceprovider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WebUser extends AbstractUser {

	private static final Logger logger = Logger.getLogger("com.gus.pattern");
	
	public static final DateFormat ISO_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat US_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	private boolean authenticated;
	private boolean disabled; 
	private String firstName, lastName, email;  
	private Date dateOfBirth;
	/**
	 * MUST have a JavaBeans no-arg constructor for XMLEncoding to work!
	 */
	public WebUser() {
		super();
	}
	/**
	 * Regular constructor:
	 * @param userId
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param dob
	 * @param email
	 */
	public WebUser(String userId, String password, String firstName, String lastName, String dob, String email) {
		super(userId,password); 
		assert(null!=firstName && firstName.length()>1);
		assert(null!=lastName && lastName.length()>1);
		assert(null!=dob && dob.length()==10);
		assert(null!=email && email.length()>4);
		setFirstName(firstName);
		setLastName(lastName);
		setDateOfBirth(dob);
		setEmail(email);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	protected void setPassword(String newPass) {
		//TODO refactor password managment out of here?
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			//ignore - setup error
		}
		md.update(newPass.getBytes());
		
		byte byteData[] = md.digest();
		
		StringBuilder hexString = new StringBuilder();
    	for (int i=0;i<byteData.length;i++) {
    		String hex=Integer.toHexString(0xff & byteData[i]);
   	     	if(hex.length()==1) hexString.append('0');
   	     	hexString.append(hex);
    	}
    	this.password = hexString.toString();
    	logger.log(Level.FINEST, "Setting new password "+newPass+" hashed value="+this.password);
		
	}

	@Override
	public boolean isAuthenticated() {
		return !isDisabled() && this.authenticated;
	}
	protected void setAuthenticated(boolean authenticated) {
		if(isDisabled()){
			this.authenticated = false;
		} else {
			this.authenticated = authenticated;
		}
	}
	
	@Override
	public boolean isDisabled() {
		return this.disabled;
	}
	protected void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public int getFailedLoginAttempts() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getLastSuccessfulLogin() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public String getDateOfBirthISO() {
		return ISO_FORMAT.format(dateOfBirth);
	}
	public String getDateOfBirthUS() {
		return US_FORMAT.format(dateOfBirth);
	}
	public void setDateOfBirth(String dateOfBirthString) {
		try {
			this.dateOfBirth = US_FORMAT.parse(dateOfBirthString);
		} catch (ParseException e) {
			try {
				this.dateOfBirth = ISO_FORMAT.parse(dateOfBirthString);
			} catch (ParseException e1) {
				logger.log(Level.SEVERE, "INVALID dateOfBirthString format \""+dateOfBirthString+"\"?",e);
			}
		}
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString() );
		sb.append(" firstName=" + getFirstName());
		sb.append(" lastName=" + getLastName() );
		sb.append(" dateOfBirth=" + getDateOfBirthISO());
		sb.append(" email=" + getEmail());
		sb.append(" disabled=" + isDisabled());
		sb.append(" authenticated=" + isAuthenticated());
		return sb.toString();
	}
	
	public boolean equals(Object other) {
		boolean rc = false;
		if(other instanceof WebUser) {
			WebUser otherWebUser = (WebUser)other;
			if(getUserId().equals(otherWebUser.getUserId())){
				rc = true;
			}
		}
		return rc;
	}
}
