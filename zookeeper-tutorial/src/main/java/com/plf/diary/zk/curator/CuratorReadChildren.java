package com.plf.diary.zk.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuratorReadChildren {

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
	public void readChildren1() throws Exception {
		// 读取子节点数据
		List<String> list = client.getChildren().forPath("/curator1");
		for (String string : list) {
			System.out.println(string);
		}

	}

	@Test
	public void readChildren2() throws Exception {
		// 异步方式读取子节点数据
		client.getChildren().inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				// 节点的路径
				System.out.println(event.getPath());

				// 事件类型
				System.out.println(event.getType());

				List<String> list = event.getChildren();
				for (String string : list) {
					System.out.println(string);
				}
			}
		}).forPath("/curator2");

		Thread.sleep(5000);

		System.out.println("结束");
	}
}
