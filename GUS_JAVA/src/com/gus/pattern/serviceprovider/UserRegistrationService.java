package com.gus.pattern.serviceprovider;
import com.gus.pattern.serviceprovider.UserRegistration;

import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

public final class UserRegistrationService {

	private static UserRegistrationService service; 
	
	private ServiceLoader<UserRegistration> loader;
	
	/**
	 * You don't instantiate me. 
	 */
	private UserRegistrationService() {
		loader = ServiceLoader.load(UserRegistration.class);
	}
	
	public static synchronized UserRegistrationService getInstance() {
		if(null==service){
			service = new UserRegistrationService();
		}
		return service;
	}
	
	public UserRegistration getUserRegistration(AbstractUser abstractUser) {
		UserRegistration provider = null;
        try {
            Iterator<UserRegistration> providers = loader.iterator();
            while (provider == null && providers.hasNext()) {
            	UserRegistration userRegistrationProvider = providers.next();
                if(userRegistrationProvider.canRegister(abstractUser)) {
                	provider = userRegistrationProvider;
                }
            }
        } catch (ServiceConfigurationError serviceError) {
            provider = null;
            serviceError.printStackTrace();

        }
        return provider;
    }
}
