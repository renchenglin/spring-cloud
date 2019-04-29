package com.example.demo;

import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class Demo {

	public static void main(String[] args) throws Exception {
		
		Watcher watcher = new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				System.out.println("event.getState(): " + event.getState());
			}
			
		};
		
		String host = "192.168.31.109:2181,192.168.31.109:2182,192.168.31.109:2183";
		final int SESSION_TIME_OUT = 2000;
		ZooKeeper zookeeper1 = new ZooKeeper(host, SESSION_TIME_OUT, watcher);
		zookeeper1.register(watcher);
		
//		ZooKeeper zookeeper2 = new ZooKeeper(host, SESSION_TIME_OUT, watcher);
		
		List<String> children = zookeeper1.getChildren("/", true);
		System.out.println(children);
		
		
		Thread.currentThread().sleep(20000);
		zookeeper1.close();
	}
	
}
