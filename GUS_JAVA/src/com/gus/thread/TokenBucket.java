package com.gus.thread;

public class TokenBucket {

	private static final TokenBucket instance = new TokenBucket();
	
	private TokenBucket() {
	}
	public static TokenBucket getInstance() {
		return instance;
	}
	/**
	 * The maximum rate of tokens issued.
	 */
	double rate = 5.0;
	/**
	 * The timespan (milliseconds) to issue tokens.
	 */
	double per  = 1000.0;
	/**
	 * The bucket of 'tokens'. 
	 */
	double allowance = rate; 	//the bucket
	
	long last_check = System.currentTimeMillis();
	/**
	 * The number of tokens issued so far in this timespan.
	 */
	long tokensIssued = 0;
	/**
	 * @return <code>true</code> if the maximum rate of issuing 'tokens' has not been exceeded  
	 * <code>false</code> otherwise.
	 */
	public synchronized boolean getToken() {
	  long current = System.currentTimeMillis();
	  long time_passed = current - last_check;
	  last_check = current;
	  allowance += time_passed * (rate / per);
	  if (allowance > rate){
		  allowance = rate; 	//reset
		  tokensIssued = 0;
	  }
	  if (allowance < 1.0) {
		  return false;
	  } else {
		  allowance -= 1.0;
		  tokensIssued++;
		  return true;
	  }
	}
	
	public synchronized long getTokensIssued() {
		return tokensIssued;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder("bucket:{");
		sb.append("issued:").append(getTokensIssued()).append(",");
		sb.append("rate:").append(rate).append(",");
		sb.append("per:").append(per).append("}");
		return sb.toString();
	}
}
