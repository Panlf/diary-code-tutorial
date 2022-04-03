package com.plf.diary.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CuratorLock {

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
	public void lock1() throws Exception {
		//排他锁
		InterProcessLock lock = new InterProcessMutex(client,"/lock1");
		System.out.println("等待获取锁对象");
		//获取锁
		lock.acquire();
		
		Thread.sleep(3000);
		
		lock.release();
		System.out.println("锁对象释放");
	}
	
	@Test
	public void lock2() throws Exception {
		//读写锁
		InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client,"/lock2");
		
		//获取读锁对象
		InterProcessLock interProcessLock = lock.readLock();
		System.out.println("等待获取锁对象");
		//获取锁
		interProcessLock.acquire();
		
		Thread.sleep(3000);
		
		interProcessLock.release();
		System.out.println("锁对象释放");
	}
	
	@Test
	public void lock3() throws Exception {
		//读写锁
		InterProcessReadWriteLock lock = new InterProcessReadWriteLock(client,"/lock3");
		
		//获取写锁对象
		InterProcessLock interProcessLock = lock.writeLock();
		System.out.println("等待获取锁对象");
		//获取锁
		interProcessLock.acquire();
		
		Thread.sleep(3000);
		
		interProcessLock.release();
		System.out.println("锁对象释放");
	}
}
