package com.iptiq.loadbalancer.service;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoadbalancerService {

	final String provider_URL = "http://provider-";

	private @Value("${invocation.method}")
	String invocationMethod;

	private @Value("${instance.per.provider}")
	int instancePerProvider;

	private @Value("${number.of.provider}")
	int numberOfProvider;

	private @Value("${array.of.provider}")
	Integer[] arrayofProvider;

	BlockingQueue<Integer> liveport_queue = new LinkedBlockingQueue<Integer>(10);
	HashMap<Integer, Integer> healthcheckMap = new HashMap<>();

	public String getUniqueProviderId() throws Exception { 
		String result = null;
		Integer port = liveport_queue.poll(100, TimeUnit.MILLISECONDS);
		if(port == null)
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Server is fully occupied");

		int counter = 1;
		while(healthcheckMap.containsKey(port)) {
			liveport_queue.put(port);
			if(counter == liveport_queue.size())
				throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Server is fully occupied");
			port = liveport_queue.poll(100, TimeUnit.MILLISECONDS);
			counter++;
		}
			result = getUniqueIdFromProviders(port);

		/** Added sleep of 1 second to imitate real time delay **/
		Thread.sleep(1000);
		liveport_queue.put(port);

		return result;
	}

	public void check() {
		Arrays.stream(arrayofProvider).forEach(
				port -> {
					try (Socket socket = 
							new Socket(new java.net.URL(provider_URL+port).getHost(),port)) {

						if(healthcheckMap.size() > 0 && healthcheckMap.containsKey(port)) {
							healthcheckMap.put(port, healthcheckMap.get(port)+1);
							if(healthcheckMap.get(port) == 2) {
								log.info("Registered after 2 success health check : "+port);
								healthcheckMap.remove(port);
							}
						}
					} catch (ConnectException e) {
						log.error("Failed to connect to: "+provider_URL+port+":"+port);
						healthcheckMap.put(port, 0);
					}
					catch (Exception ex) {
						log.error("Exception: "+ ex.getMessage() + "\n" + ex.getLocalizedMessage());
						ex.printStackTrace();
					}
				}
				);
	}

	@PostConstruct
	public void healthCheckScheduler() {
		/** Interval of 10 second to perform provider`s health check **/
		final long timeInterval = 10000;

		Runnable runnable = new Runnable() {
			public void run() {
				while (true) {
					check();

					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	@PostConstruct
	public void fillInitialPortQueue() {
		List<Integer> initiaList = Arrays.asList(arrayofProvider);
		List<Integer> concatedList = new ArrayList<Integer>();
		IntStream.range(1, instancePerProvider+1).forEach($ ->concatedList.addAll(initiaList));
		if(invocationMethod.equals("Random"))
			Collections.shuffle(concatedList);

		BlockingQueue<Integer> liveport_queue_temp = new LinkedBlockingQueue<Integer>(concatedList);
		liveport_queue = liveport_queue_temp;
	}

	public String getUniqueIdFromProviders(Integer port) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(provider_URL+port+":"+port+"/uniqueId/{port}", String.class, port);
	}
}
