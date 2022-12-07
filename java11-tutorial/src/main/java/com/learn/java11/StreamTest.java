package com.learn.java11;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class StreamTest {

	@Test
	public void StreamOfTest() {
		Stream<Integer> stream = Stream.of(12,34,1,64);
		stream.forEach(System.out::println);
		
		//报异常，传入null会被解析一个数据对象，从而访问数组长度而报异常
		//Stream.of(null);
		
		Stream<Object> streamNull = Stream.ofNullable(null);
		streamNull.forEach(System.out::println);
	}
	
	@Test
	public void newMethodTest() {
		//takeWhile dropWhile
		Stream<Integer> stream1= Stream.of(1,23,12,34,11);
		//从流中一直获取判定器中为真的元素，一旦遇到元素为假，就停止处理
		Stream<Integer> stream2 = stream1.takeWhile(t -> t % 2 != 0);
		stream2.forEach(System.out::println);
		
		System.out.println("*****************");
		Stream<Integer> stream3= Stream.of(1,23,12,34,11);
		//从流中一直剔除判定器中为真的元素，一旦遇到元素为假，就停止处理
		Stream<Integer> stream4 = stream3.dropWhile(t -> t % 2 != 0);
		stream4.forEach(System.out::println);
	}
	
	@Test
	public void iterateTest() {
		Stream<Integer> stream1= Stream.iterate(1, t->(2*t)+1);
		stream1.limit(10).forEach(System.out::println);
		
		System.out.println("*****************");
		//有限的迭代
		Stream<Integer> stream2 = Stream.iterate(1,t->t<1000, t->(2*t)+1);
		stream2.forEach(System.out::println);
		
	}
}
