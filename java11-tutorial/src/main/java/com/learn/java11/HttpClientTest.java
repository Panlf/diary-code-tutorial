package com.learn.java11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

public class HttpClientTest {

	@Test
	public void httpAsyncTest() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
				.build();
		BodyHandler<String> responseBodyHandler = BodyHandlers.ofString();
		
		try {
			CompletableFuture<HttpResponse<String>> sendAsync = client
					.sendAsync(request, responseBodyHandler);
			HttpResponse<String> response = sendAsync.get();
			String body = response.body();
			System.out.println(body);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void httpTest() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.baidu.com"))
				.build();
		BodyHandler<String> responseBodyHandler = BodyHandlers.ofString();
		try {
			HttpResponse<String> response = client.send(request,responseBodyHandler);
		
			String body = response.body();
			System.out.println(body);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
