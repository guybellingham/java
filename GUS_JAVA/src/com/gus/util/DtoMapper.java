package com.gus.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * A generic type safe DTO/Javabean properties Mapper which you can 
 * use very simply as follows: <br/>
 * <pre>
 * //Create the Dozer mapper ONCE ONLY with the From and To classes (and optional config file)
 * public static final String DOZER_CONFIG_FILE = "dozer-freightmovementservice-mappings.xml"; 
 * public static final DtoMapper<DsrLocationDTO,DsrLocation> DTO_MAPPER = new DtoMapper<DsrLocationDTO,DsrLocation>(DOZER_CONFIG_FILE);
 * ...later in my method 
 *     //Map from the DTO to the dsrLocation using my mapper
 *     DsrLocation myDsrLocation = DTO_MAPPER.mapTo(myDsrLocationDTO,DsrLocation.class);
 * </pre> 
 * <p>NOTE: Construction of a Dozer mapper is EXPENSIVE especially when 
 * you provide an XML config file. So you should minimize constructing 
 * mappers, and never construct them inside a loop. The Dozer mapper is multi thread safe.
 *    
 * <ol>
 * <li>By default it maps similarly named properties from the given <code>from</code> bean 
 * into a new <code>to</code> bean and returns it.
 * <li>It works <i>recursively</i>! So beware using it on huge object graphs! It will 
 * always be more efficient to hand code a 'mapper' utility or method to do the copying 
 * (if you have the time and can be bothered).  
 * <li>You may need to pass in a Dozer mapping configuration xml file, 
 * to tell the Dozer bean mapper the 'real' class to use for any 
 * embedded properties which are <i>interface</i> types. 
 * <li>Watch out for differences in property names and getter/setter methods. 
 * IF the <code>from</code> bean and <code>to</code> bean have slightly different
 * names for the same property or slightly different getters or setters, 
 * you can either FIX the DTO and make it <i>match exactly</i> to the other class or 
 * provide a 'mappings file' to explain to Dozer the differences.     
 * </ol>
 * <p>TIP: Write a JUnit test to make sure that your <code>from</code> and  
 * <code>to</code> beans are being fully mapped! This guy may 'skip over' 
 * anything it doesn't know how to handle.  
 *  
 * @author bellinghamsmith.guy
 * @param <F> the class of the <code>from</code> Javabean. 
 * @param <T> the class of the <code>to</code> Javabean.
 */
public class DtoMapper<F,T> {
	private static final Log log = LogFactory.getLog(DtoMapper.class);
	private Mapper mapper;
	private String mappingFile;
	/**
	 * Construct the bean mapper with 'default' property mappings.
	 */
	public DtoMapper(){
		mapper = new DozerBeanMapper();
	}
	/**
	 * Construct the mapper with the name of a Dozer mappings config xml file 
	 * @see http://dozer.sourceforge.net/documentation/usage.html
	 * @param dozerMappingFile  Name of the Dozer config.xml file
	 */
	public DtoMapper(String dozerMappingFile){
		setMappingFile(dozerMappingFile);
		if(log.isDebugEnabled()) {
			log.debug("Constructing DtoMapper with config filename="+dozerMappingFile);
		}
		if(null!=mappingFile && !mappingFile.isEmpty()){
			List<String> configFiles = new ArrayList<String>();
			configFiles.add(mappingFile);
			mapper = new DozerBeanMapper(configFiles);
		} 
	}
	/**
	 * Copies all the values from the properties in the 
	 * <code>from</code> bean into their 'corresponding' (similarly named) 
	 * properties in the <code>to</code> bean recursively. 
	 * @param from  JavaBean copied from
	 * @param clazz Class of the <code>to</code> bean.
	 * @return the <code>to</code> bean of type T
	 */
	public T mapTo(F from, Class<T> clazz) {
		assert(null!=from);
		assert(null!=clazz);
		T to = getMapper().map(from, clazz);
		if(log.isDebugEnabled()) {
			log.debug("mapTo(from="+from+",to="+to+")");
		}
		return to;
	}
	/**
	 * Copies all the values from the properties in the 
	 * <code>fromArray</code> Array into their 'corresponding' (similarly named) 
	 * properties in a new <code>toArray</code> that is returned to the caller. 
	 * @param fromArray  Array of Javabeans copied from
	 * @param clazz Class of the <code>to</code> beans in the <code>toArray</code>.
	 * @return the <code>toArray</code> containing beans of type T
	 */
	public T[] mapTo(F[] fromArray, Class<T> clazz) {
		assert(null!=fromArray);
		assert(fromArray.length>0);
		assert(null!=clazz);
		
		Mapper mapper = getMapper();
		T[] toArray  = (T[]) Array.newInstance(clazz, fromArray.length);
		for (int i = 0; i < fromArray.length; i++) {
			T to = mapper.map(fromArray[i], clazz);
			toArray[i]=to;
		}
		return toArray;
	}

	public Mapper getMapper() {	
		return mapper;
	}

	public String getMappingFile() {
		return mappingFile;
	}
	
	void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}
	
}
