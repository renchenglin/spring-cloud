package com.example.jacoco;

public class Cat implements Animal {
	public static String type = "cat";
	public String name;
	
	@Override
	public void eat(){
		System.out.println("-----------eat-----------");

	}
	
	public String getName() {
		return name;
	}
}
