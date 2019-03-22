package com.gus.hello;

public class HelloImpl implements IHello {
	
	@Override
	public String sayHello() {
		return "HELLO, from module.one!";
	}
	
	public static void doSomething(String thing) {
		System.out.println("Doing ..."+thing);
	}
}
