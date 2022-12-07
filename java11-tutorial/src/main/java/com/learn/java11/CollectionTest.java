package com.learn.java11;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class CollectionTest {

	/*
	 * 集合中的一些增强API
	 */
	@Test
	public void CollectionOfTest() {
		//不可以新添元素 Arrays.asList()同样不行
		List<String> list = List.of("aaa","bbb","ccc");
		System.out.println(list);
		
		LocalDate.of(2020, 4, 22);
		
		//添加重复元素的时候会抛出异常
		Set.of(100,50,20,30,100);
		
		Stream.of(12,43,12);
	}
}
