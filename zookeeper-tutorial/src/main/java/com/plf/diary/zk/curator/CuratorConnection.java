package com.plf.diary.zk.curator;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.apache.curator.retry.RetryUntilElapsed;
//import org.apache.curator.retry.RetryNTimes;
//import org.apache.curator.retry.RetryOneTime;

public class CuratorConnection {

	public static void main(String[] args) {
		//3秒后重连一次，只重连一次
		//RetryPolicy retryPolicy = new RetryOneTime(3000);
		
		//每3秒后重连一次，重连3次
		//RetryPolicy retryPolicy =new RetryNTimes(3, 3000);
		
		//每3秒后重连一次，总等待时间10s
		//RetryPolicy retryPolicy = new RetryUntilElapsed(10000, 3000);
		
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		
		
		CuratorFramework client = CuratorFrameworkFactory.builder()
					//IP地址端口号127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
					.connectString("127.0.0.1:2181")
					//会话超时时间
					.sessionTimeoutMs(5000)
					//重试机制
					.retryPolicy(retryPolicy)
					//命名空间
					.namespace("create")
					.build();
		
		client.start();
		
		System.out.println("已经连接");
		
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		client.close();
		
	}

}
