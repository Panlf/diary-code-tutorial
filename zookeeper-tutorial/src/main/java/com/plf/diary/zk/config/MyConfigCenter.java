package com.plf.diary.zk.config;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

public class MyConfigCenter implements Watcher{

	static CountDownLatch latch = new CountDownLatch(1);
	
	static ZooKeeper zookeeper;
	
	private String url;
	
	private String username;
	
	private String password;
	
	@Override
	public void process(WatchedEvent event) {
		try {
			if(event.getType() == EventType.None){
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
			}else if(event.getType()==EventType.NodeDataChanged){
				initValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MyConfigCenter() {
		initValue();
	}
	
	//连接zookeeper服务器，读取配置信息
	public void initValue(){
		try {
			//创建连接
			zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, this);
			//阻塞线程，等待连接的创建成功
			latch.await();
			//读取配置信息
			this.url = new String(zookeeper.getData("/config/url", true, null));
			this.password = new String(zookeeper.getData("/config/password", true, null));
			this.username = new String(zookeeper.getData("/config/username", true, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		

		//阻塞线程等待连接的创建
		latch.await();
		//会话id
		System.out.println(zookeeper.getSessionId());
		Thread.sleep(5000);
		
		zookeeper.close();
		
		System.out.println("结束");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
