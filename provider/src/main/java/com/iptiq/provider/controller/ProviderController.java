package com.iptiq.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iptiq.provider.service.ProviderService;

@RestController
@CrossOrigin
//@RequestMapping(path = "/provider")
//@Profile("provider")
public class ProviderController {

	@Autowired
	private ProviderService providerService;
	
	@GetMapping("/uniqueId/{port}")
	public String get(@PathVariable Integer port) {
		System.out.println("Inside get of ProviderController");
		return providerService.get(port);
	}
	
	@GetMapping("/")
	public String home() {
		System.out.println("Inside get of ProviderController");
		return "Hello! I am at home method";
	}
}
