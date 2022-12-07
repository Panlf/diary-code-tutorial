package com.learn.java11;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class InputStreamTest {

	@Test
	public void test() {
		var classLoader = this.getClass().getClassLoader();
		var inStream = classLoader.getResourceAsStream("file.txt");
		try(var os = new FileOutputStream("file_copy.txt")){
			inStream.transferTo(os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
