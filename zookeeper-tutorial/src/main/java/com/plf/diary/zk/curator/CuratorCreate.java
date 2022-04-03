package com.plf.diary.zk.curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuratorCreate {

	String ip = "127.0.0.1:2181";
	CuratorFramework client;

	@BeforeEach
	public void before() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

		client = CuratorFrameworkFactory.builder()
				// IP地址端口号127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
				.connectString(ip)
				// 会话超时时间
				.sessionTimeoutMs(5000)
				// 重试机制
				.retryPolicy(retryPolicy)
				// 命名空间
				.namespace("create").build();

		client.start();
	}

	@AfterEach
	public void after() {
		client.close();
	}

	@Test
	public void create1() throws Exception{
		//新增节点
		client.create()
		.withMode(CreateMode.PERSISTENT)
		.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
		.forPath("/curator1","curator1".getBytes());
		
		System.out.println("结束");
	}
	
	@Test
	public void create2() throws Exception{
		//自定义权限列表
		//权限列表
		List<ACL> list = new ArrayList<>();
		//授权模式和授权对象
		Id id = new Id("ip","192.168.0.4");
		list.add(new ACL(ZooDefs.Perms.ALL,id));
		//新增节点
		client.create()
		.withMode(CreateMode.PERSISTENT)
		.withACL(list)
		.forPath("/curator2","curator2".getBytes());
		
		System.out.println("结束");
	}
	
	@Test
	public void create3() throws Exception{
		//递归新增节点
		client.create()
		.creatingParentContainersIfNeeded()
		.withMode(CreateMode.PERSISTENT)
		.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
		.forPath("/curator3/curator31","curator3".getBytes());
		
		System.out.println("结束");
	}
	
	@Test
	public void create4() throws Exception{
		//异步新增节点
		client.create()
		.creatingParentContainersIfNeeded()
		.withMode(CreateMode.PERSISTENT)
		.withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
		.inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				//节点的路径
				System.out.println(event.getPath());
				
				//事件类型
				System.out.println(event.getType());
			}
		})
		.forPath("/curator4/curator41","curator4".getBytes());
		Thread.sleep(5000);
		System.out.println("结束");
	}
}
