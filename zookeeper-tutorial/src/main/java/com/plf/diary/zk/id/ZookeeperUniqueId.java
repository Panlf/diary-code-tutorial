package com.plf.diary.zk.id;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZookeeperUniqueId implements Watcher{

	String Ip ="127.0.0.1:2181";
	
	CountDownLatch latch = new CountDownLatch(1);
	
	String defaultPath = "/uniqueId";
	
	ZooKeeper zookeeper;
	
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
					System.out.println("连接超时!");
				}else if(event.getState() == Event.KeeperState.AuthFailed){
					System.out.println("认证失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public ZookeeperUniqueId(){
		try {
			zookeeper = new ZooKeeper(Ip, 5000, this);
			
			//阻塞线程，等待连接的创建成功
			latch.await();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getUniqueId(){
		String path="";
		
		try {
			//创建临时有序节点
			path = zookeeper.create(defaultPath, new byte[0], Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return path.substring(9);
	}

}
