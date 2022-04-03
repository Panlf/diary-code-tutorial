package com.plf.diary.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuratorRead {

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
	public void read1() throws Exception{
		//读取节点数据
		byte[] data = client.getData()
					.forPath("/curator1");
		System.out.println(new String(data));
	}
	
	@Test
	public void read2() throws Exception{
		Stat stat = new Stat();
		//读取节点数据
		byte[] data = client.getData()
					.storingStatIn(stat)
					.forPath("/curator2");
		System.out.println(new String(data));
		System.out.println(stat.getVersion());
	}
	
	@Test
	public void read3() throws Exception{
		//异步方式修改节点数据
		client.getData()
			.inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				//节点的路径
				System.out.println(event.getPath());
				
				//事件类型
				System.out.println(event.getType());
				
				System.out.println(new String(event.getData()));
			}
		})
		.forPath("/curator3");
		
		Thread.sleep(5000);
		
		System.out.println("结束");
	}
}
