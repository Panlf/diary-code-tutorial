package com.learn.java11;

import org.junit.jupiter.api.Test;

public class StringTest {

	@Test
	public void countLinesTest() {
		String lines = "11\r\n22\r\n33";
		lines.lines().forEach(System.out::println);
		System.out.println(lines.lines().count());
	}
	
	@Test
	public void repeatTest() {
		//字符串重复
		System.out.println("Java".repeat(5));
	}
	
	@Test
	public void blankTest() {
		String string = "\t  \r\n";
		//判断字符串是否都是空白
		System.out.println(string.isBlank());
		
		System.out.println("***************");
		string = "\t \r\n abc \t ";
		//去重字符串首尾的空白，包括英文和其他所有语言中的空白字符
		String string2 = string.strip();
		System.out.println(string2.length());
		
		//去除字符串首尾的空白字符 只能去除码值小于等于32的空白字符
		String string3 = string.trim();
		System.out.println(string3);
		System.out.println(string3.length());
		
		//去重字符串尾部的空白
		string.stripTrailing();
		//去除字符串头部的空白
		string.stripLeading();
	}
}
