package com.gus.pattern.serviceprovider;

public class UserIdExistsException extends Exception {

	public UserIdExistsException(){ }
	public UserIdExistsException(String message){
		super(message);
	}
}
