/**
 * This is the consuming module.
 * @see  
 */
module app {
	/**
	 * This declares both a runtime and a compile-time dependency on the 'hello' module. 
	 * All public types exported from this dependency are accessible by our 'app' module. 
	 */
    requires hello;
    
    /**
     * If we want to references another module (say some 'optional' util or logging module), 
     * that users of our library will never want to use, we can say 
     * <code>requires static {module.name}</code> which will create a compile-time-only dependency.
     */
    
    /**
     * If our module has a 'required' or ‘transitive’ dependency (in order to work) on a module  
     * we can declare that module with <code>requires transitive {module.name}</code>. 
     * Now when a developer 'requires' this module they won't also have also to say requires   
     * {all the required module.names} for this module.    
     */
    
    /**
     * We can designate the 'services' our module consumes with the <code>uses</code> directive.
     * Note that the class name we use is either the interface or abstract class of the service, not the implementation class.
     */
    uses com.gus.hello.IHello;
}