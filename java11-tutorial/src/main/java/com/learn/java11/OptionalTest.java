package com.learn.java11;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class OptionalTest {

	@Test
	public void test() {
		//of方法中如果传入的参数是null，会抛出空指针异常
		//Optional<String> optional = Optional.of(null);
		//ofNullable可以兼容空指针，但是实际传入null后要小心
		Optional<Object> optional = Optional.ofNullable(null);
		//如果内部引用为空，则返回参数中的引用，否则返回内部引用
		Object object = optional.orElse("abc");
		System.out.println(object);
		
		Object object2 = optional.orElseThrow();
		System.out.println(object2);
	}
}
