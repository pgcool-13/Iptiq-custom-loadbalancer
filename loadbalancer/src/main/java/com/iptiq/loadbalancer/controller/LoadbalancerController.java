package com.iptiq.loadbalancer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iptiq.loadbalancer.service.LoadbalancerService;

@RestController
@CrossOrigin
public class LoadbalancerController {

	@Autowired
	private LoadbalancerService loadbalancerService;

	@GetMapping("/uniqueId")
	public String get() throws Exception {
			return loadbalancerService.getUniqueProviderId();
	}

	@GetMapping("/healthcheck")
	public void healthCheck() {
		loadbalancerService.check();
	}

}
