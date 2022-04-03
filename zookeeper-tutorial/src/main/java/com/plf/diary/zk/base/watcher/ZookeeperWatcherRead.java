package com.plf.diary.zk.base.watcher;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class ZookeeperWatcherRead{

	String IP = "127.0.0.1:2181";
	ZooKeeper zookeeper;

	@BeforeEach
	public void before() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);

		zookeeper = new ZooKeeper(IP, 5000, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == Event.KeeperState.SyncConnected) {
					System.out.println("连接创建成功");
					latch.countDown();
				}
			}
		});

		// 主线程阻塞等待连接对象的创建成功
		latch.await();
	}

	@AfterEach
	public void after() throws Exception {
		zookeeper.close();
	}
	
	@Test
	public void watcherRead1() throws Exception {
		//使用连接对象中的watcher
		zookeeper.getData("/watcher1", true,null);
		Thread.sleep(5000);
		System.out.println("结束");
	}
	
	@Test
	public void watcherRead2() throws Exception {
		//使用连接对象中的watcher
		zookeeper.getData("/watcher2", new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义的watcher");
				System.out.println("path="+event.getPath());
				System.out.println("eventType="+event.getType());
			}
		},null);
		Thread.sleep(5000);
		System.out.println("结束");
	}
	
	@Test
	public void watcherRead3() throws Exception {
		//watcher是一次性的
		Watcher watcher = new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义的watcher");
				System.out.println("path="+event.getPath());
				System.out.println("eventType="+event.getType());
				if(event.getType()==Event.EventType.NodeDataChanged){
					try {
						zookeeper.getData("/watcher3", this,null);
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		//使用连接对象中的watcher
		zookeeper.getData("/watcher3", watcher,null);
		Thread.sleep(5000);
		System.out.println("结束");
	}
	
	
	@Test
	public void watcherRead4() throws Exception {
		//注册多个监听器对象
		
		//使用连接对象中的watcher
		zookeeper.getData("/watcher4", new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义的watcher1");
				System.out.println("path="+event.getPath());
				System.out.println("eventType="+event.getType());
				if(event.getType()==Event.EventType.NodeDataChanged){
					try {
						zookeeper.getData("/watcher4", this,null);
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		},null);
		
		
		zookeeper.getData("/watcher4", new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				System.out.println("自定义的watcher2");
				System.out.println("path="+event.getPath());
				System.out.println("eventType="+event.getType());
				if(event.getType()==Event.EventType.NodeDataChanged){
					try {
						zookeeper.getData("/watcher4", this,null);
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		},null);
		Thread.sleep(5000);
		System.out.println("结束");
	}
}
