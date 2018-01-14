package com.gus.pattern.serviceprovider;

import com.gus.nio.FileServices;
import com.gus.nio.FileServicesImpl;

public enum Services {
	File(FileServices.class,FileServicesImpl.class),
	Map(Object.class,Object.class),  
	Geocoding(AddressGeocoding.class,AddressGeocodingService.class),
	Timezone(LatLongTimezone.class,LatLongTimezoneService.class),
	Regsitration(UserRegistration.class,UserRegistrationService.class);
	
	private Class interfaceType;
	private Class[] serviceTypes;
	
	Services(Class interfaceType, Class... serviceTypes) {
		setInterfaceType(interfaceType);
		setServiceTypes(serviceTypes);
	}

	public Class getInterfaceType() {
		return interfaceType;
	}

	private void setInterfaceType(Class interfaceType) {
		this.interfaceType = interfaceType;
	}

	public Class[] getServiceTypes() {
		return serviceTypes.clone();
	}

	private void setServiceTypes(Class[] serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
}
