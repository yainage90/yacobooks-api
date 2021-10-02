package com.yaincoding.yacobooksapi.slack;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SlackLogBot {

	private static final String SLACK_WEBHOOK_URL = System.getenv("SLACK_WEBHOOK_URL");

	public static void sendError(Throwable error) {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);
		httpPost.addHeader("Content-Type", "application/json");

		JsonObject body = new JsonObject();
		body.addProperty("text", error.getMessage());
		String bodyString = body.toString();
		StringEntity entity = new StringEntity(bodyString, StandardCharsets.UTF_8);

		httpPost.setEntity(entity);

		try {
			httpClient.execute(httpPost);
		} catch (IOException e) {
		}
	}

	public static void sendMessage(String msg) {
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(SLACK_WEBHOOK_URL);
		httpPost.addHeader("Content-Type", "application/json");

		JsonObject body = new JsonObject();
		body.addProperty("text", msg);
		String bodyString = body.toString();
		StringEntity entity = new StringEntity(bodyString, StandardCharsets.UTF_8);

		httpPost.setEntity(entity);

		try {
			httpClient.execute(httpPost);
		} catch (IOException e) {
		}
	}
}
