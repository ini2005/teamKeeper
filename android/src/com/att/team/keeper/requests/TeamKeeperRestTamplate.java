package com.att.team.keeper.requests;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class TeamKeeperRestTamplate extends RestTemplate {

	private final static int HTTP_CONNECT_TIMEOUT = 15 * 1000;
	private final static int HTTP_READ_TIMEOUT = 25 * 1000;

	public TeamKeeperRestTamplate() {
		

		 HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new
		 HttpComponentsClientHttpRequestFactory();
		 clientHttpRequestFactory.setReadTimeout(HTTP_READ_TIMEOUT);
		 clientHttpRequestFactory.setConnectTimeout(HTTP_CONNECT_TIMEOUT);

//		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectTimeout(HTTP_CONNECT_TIMEOUT);
//		clientHttpRequestFactory.setReadTimeout(HTTP_READ_TIMEOUT);

		this.setRequestFactory(clientHttpRequestFactory);
		

	}
}
