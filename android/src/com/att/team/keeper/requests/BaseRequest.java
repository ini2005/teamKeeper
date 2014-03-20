package com.att.team.keeper.requests;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.NestedRuntimeException;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.os.Handler;
import android.util.Log;

/**
 * The Class BaseRequest.
 * 
 * @param <T>
 *            the generic type of the body object
 * @param <V>
 *            the value type of the return object
 */
public abstract class BaseRequest<T, V> {

	/**
	 * Holds HTTP headers used in the HTTP requests and responses.<br/>
	 * <br/>
	 */
	protected interface Headers {
		public final String CONNECTION = "Connection";
	}

	/**
	 * Holds HTTP header values used in the HTTP requests and responses.<br/>
	 * <br/>
	 */
	protected interface HeaderValues {
		public final String CONNECTION_KEEP_ALIVE = "Keep-Alive";
	}

	/** holds the tag for the class. */
	protected final String TAG = getClass().getSimpleName();

	/** The JSON media type. */
	private final MediaType JSON_MEDIA_TYPE = new MediaType("application",
			"json");

	/** The Network thread. */
	private NetworkThread mNetworkThread;

	/** The Handler. */
	private Handler mHandler;

	/**
	 * Instantiates a new base green ride request.
	 */
	public BaseRequest(NetworkThread networkThread) {
		mNetworkThread = networkThread;
		mHandler = new Handler(mNetworkThread.getLooper());
	}

	/**
	 * Runs the specified action on the Network thread. If the current thread is
	 * the Network thread, then the action is executed immediately. If the
	 * current thread is not the Network thread, the action is posted to the
	 * event queue of the Network thread.
	 * 
	 * @param action
	 *            the action to run on the UI thread
	 */
	public final void runOnNetworkThread(Runnable action) {
		if (Thread.currentThread() != mNetworkThread) {
			mHandler.post(action);
		} else {
			action.run();
		}
	}

	/**
	 * Gets the method.
	 * 
	 * @return the method
	 */
	public abstract HttpMethod getMethod();

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public abstract String getUrl();

	/**
	 * Gets the headers for the request.<br/>
	 * 
	 * @return The headers for the request, as mapping between header name to
	 *         header value.
	 */
	public abstract Map<String, String> getHeaders();

	/**
	 * Gets the request body.
	 * 
	 * @return the request body
	 */
	public abstract T getRequestBody();

	/**
	 * Gets the response class.
	 * 
	 * @return the response class
	 */
	public abstract Class<V> getResponseClass();

	protected abstract boolean checkForLatestConfigVersion();

	/**
	 * Execute.
	 * 
	 * @return the v
	 */
	public void execute(final IRequestResult<V> requestResult) {

		runOnNetworkThread(new Runnable() {

			@Override
			public void run() {

				try {

					// Add the identity Accept-Encoding header
					HttpHeaders requestHeaders = new HttpHeaders();
					// add Accept Encoding header to the request
					requestHeaders
							.setAcceptEncoding(ContentCodingType.IDENTITY);
					// add Accept header JSON to the request
					requestHeaders.setAccept(Collections
							.singletonList(JSON_MEDIA_TYPE));

					Map<String, String> headers = getHeaders();
					if (headers != null && headers.isEmpty() == false) {
						for (Entry<String, String> entry : headers.entrySet()) {
							requestHeaders
									.add(entry.getKey(), entry.getValue());
						}
					}


					T body = getRequestBody();
					if (body != null) {
						requestHeaders.setContentType(JSON_MEDIA_TYPE);
					}
					// create the http request with the body
					HttpEntity<T> requestEntity = new HttpEntity<T>(body,
							requestHeaders);

					// Create a new RestTemplate instance
					RestTemplate restTemplate = new TeamKeeperRestTamplate();

					// Add the JACKSON and String message converters
					restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

					final ResponseEntity<V> responseEntity;
					try {

						// Make the HTTP POST request, marshaling the request to
						// JSON, and the response to a String
						String requestUrl = getUrl();
						HttpMethod requestHttpMethod = getMethod();
						Class<V> responsClass = getResponseClass();

						Log.d(TAG,String.format("Try to send requset url=[%s], method=[%s], Headers=%s",requestUrl, requestHttpMethod,requestHeaders));

						if (getRequestBody() != null) {
							Log.d(TAG, String.format("Request Body:\n%s", requestEntity.getBody().toString()));
						}

						
						responseEntity = restTemplate.exchange(requestUrl,
								requestHttpMethod, requestEntity, responsClass);

					} catch (NestedRuntimeException e) {
						Log.e(TAG,
								"unable to get reponse from the server", e);
						requestResult.getCallbackHandler().post(new Runnable() {

							@Override
							public void run() {requestResult.onFailed(500,null);
							}
						});

						return;
					}

					Log.d(TAG, String.format(
							"Received response: Code = %s, Headers=%s",
							responseEntity.getStatusCode(),
							responseEntity.getHeaders()));

					if (responseEntity.hasBody()) {
						Log.d(TAG, String.format("Response Body:\n%s",
								responseEntity.getBody()));
					}

					if (responseEntity.getStatusCode() != HttpStatus.OK) {
						Log.e(TAG,"unable to get reponse from the server, code="
										+ responseEntity.getStatusCode());
						requestResult.getCallbackHandler().post(new Runnable() {

							@Override
							public void run() {requestResult.onFailed(500,null);
							}
						});
						return;
					}

					requestResult.getCallbackHandler().post(new Runnable() {

						@Override
						public void run() {
							requestResult.onSuccess(responseEntity.getBody());
						}
					});

				} finally {
				}
			}
		});

	}

	/**
	 * The Interface IRequestResult.
	 * 
	 * @param <V>
	 *            the value type
	 */
	public interface IRequestResult<V> {

		public void onSuccess(V obj);

		public void onNetworkIsOffline();

		public void onFailed(int responseCode, V obj);

		public Handler getCallbackHandler();

	}

}
