package com.example.jacoco;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author wangmengjun
 * 
 */
public class Calculator {
	
	ThreadLocal<String> local = new ThreadLocal<>();

	public int add(int a, int b) {
		return a + b;
	}

	public int sub(int a, int b) {
		return a - b;
	}
	
	public static void main(String[] args) throws InterruptedException {
		/*ThreadLocal<Integer[]> local = new ThreadLocal<>();
		Integer [] integers = new Integer[2000000];
		for(int i=0;i<200000;i++) {
			integers[i] = i;
		}
		
		local.set(integers);
		System.gc();
//		Thread.currentThread().sleep(5 * 1000);
		
		
		System.out.println(local.get()[1]);
		System.gc();
		Thread.currentThread().sleep(5 * 1000);
		
		
		System.out.println(local.get());*/
		
		Map<String,String> map = new HashMap<>();
		for(int i=0;i<100;i++) {
			map.put(i + "", i +"");
		}
		
		Set<String> keyset = map.keySet();
		
		System.out.println(keyset);
		
	}
}
