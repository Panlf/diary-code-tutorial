package com.plf.diary.zk.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ZookeeperLocks {
	String Ip ="127.0.0.1:2181";
	
	CountDownLatch latch = new CountDownLatch(1);
	
	ZooKeeper zookeeper;
	
	private static final String LOCK_ROOT_PATH = "/locks";
	
	private static final String LOCK_NODE_NAME="lock";

	private String lockPath;
	
	//获取锁
	public void acquireLock() throws Exception {
		//创建锁节点
		createLock();
		
		//尝试获取锁
		attemptLock();
		
		
	}
	
	public ZookeeperLocks() {
		try {
			zookeeper = new ZooKeeper(Ip, 5000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if(event.getType() == Event.EventType.None){
						// 建立连接
						if (event.getState() == Event.KeeperState.SyncConnected) {
							System.out.println("连接成功!");
							latch.countDown();
						}
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//创建锁节点
	private void createLock() throws Exception{
		//判断Locks是否存在
		Stat stat = zookeeper.exists(LOCK_ROOT_PATH, false);
		if(stat == null){
			zookeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		
		lockPath = zookeeper.create(LOCK_ROOT_PATH+"/"+LOCK_NODE_NAME, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("节点创建成功:"+lockPath);
	}
	
	
	//监视器对象，监视上一个节点是否删除
	Watcher watcher = new Watcher(){

		@Override
		public void process(WatchedEvent event) {
			if(event.getType() == Event.EventType.NodeDeleted){
				synchronized (this) {
					notifyAll();
				}
			}
		}
		
	};
	//尝试获取锁
	private void attemptLock() throws Exception {
		//获取Locks节点下的所有子节点
		List<String> list = zookeeper.getChildren(LOCK_ROOT_PATH, false);
		//对子节点进行排序
		Collections.sort(list);
		int index = list.indexOf(lockPath.substring(LOCK_ROOT_PATH.length()+1));
		if(index == 0){
			System.out.println("获取锁成功");
			return;
		}else{
			//上一个节点的路径
			String path = list.get(index-1);
			Stat stat = zookeeper.exists(LOCK_ROOT_PATH+"/"+path, watcher);
			if(stat == null){
				attemptLock();
			}else{
				synchronized (watcher) {
					watcher.wait();
				}
				attemptLock();
			}
		}
		
	}
	
	public void releaseLock() throws Exception{
		zookeeper.delete(this.lockPath, -1);
		zookeeper.close();
		System.out.println("锁已经释放:"+this.lockPath);
	}
}
