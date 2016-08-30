package com.gus.thread;

/**
 * Use TokenBucket as a v.simple way to limit the rate at which 
 * virtual 'tokens' are issued {@link #getToken()} to one or more threads 
 * that are consuming some resource at an indeterminate rate. 
 * <pre>
 * TokenBucket bucket = TokenBucket.getInstance();   //thread safe
 * if(bucket.get()) {
 *    //consume some resource at a determined rate
 * } else {
 *    //wait or do something else
 * }
 * </pre>
 * Note: The {@link #getToken()} method will not block the calling Thread. 
 * @author Guy
 */
public class TokenBucket {

	/**
	 * The maximum rate at which tokens can be issued in any one timespan.
	 */
	long rate = 5; 
	/**
	 * The timespan defaults to one minute in which to issue a maximum {@value #rate} 'tokens'.
	 */
	long timespan  = 1000;	
	/**
	 * The atomic bucket of 'tokens'. 
	 */
	long tokens = rate;
	/**
	 * The number of tokens issued - mostly for testing/verification
	 */
	long issued = 0;
	
	long last_check = 0;
	
	/**
	 * The Singleton instance.
	 */
	private static final TokenBucket instance = new TokenBucket();
	private TokenBucket() {
	}
	public static TokenBucket getInstance() {
		return instance;
	}
	
	
	/**
	 * Get a 'token' from the 'bucket' of tokens available in every timespan. 
	 * Tokens are 'issued' at a maximum {@link #rate} over a certain timespan {@link #timespan}.
	 * @return <code>true</code> if the maximum rate of issuing 'tokens'  
	 * has not been exceeded, <code>false</code> if we've run out of 'tokens' for the time being.
	 */
	public boolean get() {
		if(rate > 0) {
			long current_time = System.currentTimeMillis();
			synchronized (this) {
				long time_passed = current_time - last_check;
				if(time_passed >= timespan) {
					tokens = rate;  			//refill bucket
					last_check = current_time;
					//TODO Poll ConfigUtils for any new rate value?
					//System.out.println("R");
				}
			}
			if(tokens > 0) {
				tokens--;  	//issue 1 token
				issued++;
				return true;			
			} else {
				return false;
			}
			
		} else {
			return true;
		}
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("bucket:{");
		sb.append("rate:").append(rate).append(",");
		sb.append("per:").append(timespan).append(",");
		sb.append("issued:").append(issued).append(",");
		sb.append("remaining:").append(tokens).append("}");
		return sb.toString();
	}

}
