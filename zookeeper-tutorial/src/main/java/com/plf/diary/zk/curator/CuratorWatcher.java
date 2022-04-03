package com.plf.diary.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuratorWatcher {

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
	public void watcher1() throws Exception {
		// 监视某个节点的数据变化
		final TreeCache treeCache = new TreeCache(client, "/watcher1");
		// 启动监视器对象
		treeCache.start();

		treeCache.getListenable().addListener(new TreeCacheListener() {

			@Override
			public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
				ChildData eventData = treeCacheEvent.getData();
				switch (treeCacheEvent.getType()) {
				case NODE_ADDED:
					System.out.println(
							eventData.getPath() + "节点添加" + eventData.getPath() + "\t添加数据为：" + new String(eventData.getData()));
					break;
				case NODE_UPDATED:
					System.out.println(eventData.getPath() + "节点数据更新\t更新数据为：" + new String(eventData.getData())
							+ "\t版本为：" + eventData.getStat().getVersion());
					break;
				case NODE_REMOVED:
					System.out.println(eventData.getPath() + "节点被删除");
					break;
				default:
					break;
				}
			}
		});
		
		Thread.sleep(100000);
		//关闭监视器对象
		treeCache.close();
	}
}
