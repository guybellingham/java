/**
 * By default, a module doesn’t expose any of its API to other modules. 
 * This strong encapsulation was one of the key motivators for creating the module system.
 * <li>To grant <i>runtime-only</i> access for reflection we can use the <code>open</code> 
 * directive to 'open' the entire module for reflection!
 */
module hello {
	/**
	 * This exposes all public members of the named package to other modules.
	 * By default in Java 9+, we will only have access to public classes, methods, and fields in our exported packages. 
	 */
    exports com.gus.hello;
    /**
     * To grant <i>runtime-only</i> access for reflection (to expose specific packages)
     * we can use the <code>opens</code> directive.
     */
    opens com.gus.hello;
    
    /**
     * A module can also be a service provider that other modules can consume.
	 * The first part of the directive is the provides keyword. Here is where we put the interface or abstract class name.
	 * Next, we have the with directive where we provide the implementation class name that either implements the interface or extends the abstract class.
     */
    provides com.gus.hello.IHello with com.gus.hello.HelloImpl;
}