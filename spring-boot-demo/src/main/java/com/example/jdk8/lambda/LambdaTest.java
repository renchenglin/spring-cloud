package com.example.jdk8.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.junit.Test;

public class LambdaTest {
	@Test
	public void forEach() {
		String[] atp = {"Rafael Nadal", "Novak Djokovic",  
				"Stanislas Wawrinka",  
				"David Ferrer","Roger Federer",  
				"Andy Murray","Tomas Berdych",  
		"Juan Martin Del Potro"};  
		List<String> players =  Arrays.asList(atp);  
		
		// 以前的循环方式  
		for (String player : players) {  
			System.out.print(player + "; ");  
		}  
		System.out.println("\n---------------------------");
		// 使用 lambda 表达式以及函数操作(functional operation)  
		players.forEach((String player) -> System.out.print(player + "; "));  
		System.out.println("\n---------------------------");
		// 在 Java 8 中使用双冒号操作符(double colon operator)  
		players.forEach(System.out::println);
		
		System.out.println("\n---------------------------");
	}
	
	@Test
	public void namelessClass() {
		// 1.1使用匿名内部类  
		new Thread(new Runnable() {  
			@Override  
			public void run() {  
				System.out.println("Hello world 1!");  
			}  
		}).start();  
		
		// 1.2使用 lambda expression  
		new Thread(() -> System.out.println("Hello world 2!")).start();  
		
		// 2.1使用匿名内部类  
		Runnable race1 = new Runnable() {  
			@Override  
			public void run() {  
				System.out.println("Hello world race1 !");  
			}  
		};  
		
		// 2.2使用 lambda expression  
		Runnable race2 = () -> System.out.println("Hello world race2 !");  
		
		// 直接调用 run 方法(没开新线程哦!)  
		race1.run();  
		race2.run();  
	}
	
	
	@Test
	public void collectionSort() {
		
		String[] players = {"Rafael Nadal", "Novak Djokovic",   
				"Stanislas Wawrinka", "David Ferrer",  
				"Roger Federer", "Andy Murray",  
				"Tomas Berdych", "Juan Martin Del Potro",  
				"Richard Gasquet", "John Isner"};  
		
		// 1.1 使用匿名内部类根据 name 排序 players  
		Arrays.sort(players, new Comparator<String>() {  
			@Override  
			public int compare(String s1, String s2) {  
				return (s1.compareTo(s2));  
			}  
		});
		
		// 1.2 使用 lambda expression 排序 players  
		Comparator<String> sortByName = (String s1, String s2) -> (s1.compareTo(s2));  
		Arrays.sort(players, sortByName);  
		
		// 1.3 也可以采用如下形式:  
		Arrays.sort(players, (String s1, String s2) -> (s1.compareTo(s2)));  
	}
	
	
	@Test
	public void variableScope(){
		String salutation = "Hello! ";
		GreetingService greetService1 = message -> System.out.println(salutation + message);
//		salutation = "Hello1! ";//变量会自动加final，此处取消注释会编译不通过
		greetService1.sayMessage("Runoob");
	}
	
	interface GreetingService {
		void sayMessage(String message);
	}
	
	@Test
	public void testConcurrentNavigableMap() {
		ConcurrentNavigableMap<User, String> map = new ConcurrentSkipListMap<User, String>();  
		
		map.put(new User("1"), "one");  
		map.put(new User("2"), "two");  
		map.put(new User("3"), "three");  

		ConcurrentNavigableMap<User, String> tailMap = map.tailMap(new User("2"));
		System.out.println("tailMap: " + tailMap);
		
		ConcurrentNavigableMap<User, String> headMap = map.headMap(new User("2"));
		System.out.println("headMap: " + headMap);
		
		ConcurrentNavigableMap<User, String> subMap = map.subMap(new User("2"), new User("3")); 
		System.out.println("subMap: " + subMap);
	}
	class User implements Comparable<User>{
		String name;
		User(String name){
			this.name = name;
		}
		@Override
		public int compareTo(User o) {
			return this.name.compareTo(o.name);
		}
	}
	
	   

}
