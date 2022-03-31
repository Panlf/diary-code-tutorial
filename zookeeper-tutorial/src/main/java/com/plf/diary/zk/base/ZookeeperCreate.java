package com.plf.diary.zk.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZookeeperCreate {

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
	public void create1() throws Exception {
		zookeeper.create("/create/test1", "create-Test1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
	}

	@Test
	public void create2() throws Exception {
		zookeeper.create("/create/test2", "create-Test2".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE,
				CreateMode.PERSISTENT);
	}

	@Test
	public void create3() throws Exception {
		// world授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("world", "anyone");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.READ, id));
		acls.add(new ACL(ZooDefs.Perms.WRITE, id));
		zookeeper.create("/create/test3", "create-Test3".getBytes(), acls, CreateMode.PERSISTENT);
	}

	@Test
	public void create4() throws Exception {
		// ip授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("ip", "192.168.0.1");
		// 权限设置
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		zookeeper.create("/create/test4", "create-Test4".getBytes(), acls, CreateMode.PERSISTENT);
	}

	@Test
	public void create5() throws Exception {
		// auth授权模式
		// 添加授权用户
		zookeeper.addAuthInfo("digest", "test5:123456".getBytes());
		zookeeper.create("/create/test5", "create-Test5".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL,
				CreateMode.PERSISTENT);
	}

	@Test
	public void create6() throws Exception {
		// auth授权模式
		// 添加授权用户
		zookeeper.addAuthInfo("digest", "test6:123456".getBytes());
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("auth", "test6");
		acls.add(new ACL(ZooDefs.Perms.READ, id));
		zookeeper.create("/create/test6", "create-Test6".getBytes(), acls, CreateMode.PERSISTENT);
	}

	@Test
	public void create7() throws Exception {
		// digest授权模式
		// 权限列表
		List<ACL> acls = new ArrayList<>();
		// 授权模式和授权对象
		Id id = new Id("digest", "test7:123456");
		acls.add(new ACL(ZooDefs.Perms.ALL, id));
		zookeeper.create("/create/test7", "create-Test7".getBytes(), acls, CreateMode.PERSISTENT);
	}

	@Test
	public void create8() throws Exception {
		zookeeper.create("/create/test8", "create-Test8".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
				new AsyncCallback.StringCallback() {
					
					@Override
					public void processResult(int rc, String path, Object ctx, String name) {
						//rc 0 代表创建成功
						//path 节点的路径
						//ctx 上下文参数
						//name 节点的路径
					}
				},"I am context");
		Thread.sleep(5000);
		System.out.println("结束");
	}
}
