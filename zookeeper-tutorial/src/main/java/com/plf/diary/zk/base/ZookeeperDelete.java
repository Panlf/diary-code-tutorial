package com.plf.diary.zk.base;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZookeeperDelete {

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
	public void delete1() throws Exception {
		//数据版本号	-1代表删除节点时不考虑版本信息
		zookeeper.delete("/delete/test1", -1);
	}
	
	@Test
	public void delete2() throws Exception {
		zookeeper.delete("/delete/test2", -1,new AsyncCallback.VoidCallback() {
			
			@Override
			public void processResult(int rc, String path, Object ctx) {
				//rc 0 代表删除成功
				//path 节点的路径
				//ctx 上下文参数
			}
		},"I am context");
		Thread.sleep(5000);
		System.out.println("结束");
	}
}
