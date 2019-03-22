package com.gus.app;

import com.gus.hello.IHello;
import com.gus.hello.HelloImpl;

public class MainApp {
	
	public static void main(String[] args) {
		MainApp main = new MainApp();
		main.sayHello();
		
		HelloImpl.doSomething("something");
	}
	
	public void sayHello() {
		IHello helloOne = new HelloImpl();
		System.out.println(helloOne.sayHello());
	}
}
