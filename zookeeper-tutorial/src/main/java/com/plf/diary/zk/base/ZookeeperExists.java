package com.plf.diary.zk.base;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZookeeperExists {

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
	public void exist1() throws Exception {
		Stat stat = zookeeper.exists("/exist1", false);
		System.out.println(stat.getVersion());
	}
	
	
	@Test
	public void exist2() throws Exception {
		zookeeper.exists("/exist2", false,new AsyncCallback.StatCallback() {
			
			@Override
			public void processResult(int rc, String path, Object ctx,Stat stat) {
				//rc 0 代表执行成功
				//path 节点的路径
				//ctx 上下文参数
				//节点的版本信息
				System.out.println(stat.getVersion());
			}
		},"I am context");
		Thread.sleep(5000);
		System.out.println("结束");
	}
}
