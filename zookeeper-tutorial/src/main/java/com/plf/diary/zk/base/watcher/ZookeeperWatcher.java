package com.plf.diary.zk.base.watcher;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperWatcher implements Watcher{

	static CountDownLatch latch = new CountDownLatch(1);
	
	static ZooKeeper zookeeper;
	
	@Override
	public void process(WatchedEvent event) {
		try {
			if(event.getType() == Event.EventType.None){
				if(event.getState() == Event.KeeperState.SyncConnected){
					System.out.println("连接创建成功!");
					latch.countDown();
				}else if(event.getState() == Event.KeeperState.Disconnected){
					System.out.println("断开连接!");
				}else if(event.getState() == Event.KeeperState.Expired){
					System.out.println("会话超时!");
				}else if(event.getState() == Event.KeeperState.AuthFailed){
					System.out.println("认证失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	public static void main(String[] args) throws Exception {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperWatcher());

		//阻塞线程等待连接的创建
		latch.await();
		//会话id
		System.out.println(zookeeper.getSessionId());
		Thread.sleep(5000);
		
		zookeeper.close();
		
		System.out.println("结束");
	}
}
