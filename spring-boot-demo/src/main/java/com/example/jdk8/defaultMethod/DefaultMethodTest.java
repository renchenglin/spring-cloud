package com.example.jdk8.defaultMethod;

import org.junit.Test;

public class DefaultMethodTest implements Vehicle, FourWheeler {

	@Override
	@Test
	public void print() {
		FourWheeler.super.print();
	}
	
	@Test
	public void doGo() {
		this.go();
	}
	
	@Test
	public void doBlowHorn() {
		FourWheeler.blowHorn();
	}

}


interface Vehicle {
	default void print(){
		System.out.println("我是一辆车!");
	}
}

interface FourWheeler {
	default void print(){
		System.out.println("我是一辆四轮车!");
	}
	
	default void go(){
		System.out.println("我是一辆四轮车! go go go");
	}
	
	/**
	 * 申明静态方法
	 */
	static void blowHorn(){
		System.out.println("按喇叭!!!");
	}
}
