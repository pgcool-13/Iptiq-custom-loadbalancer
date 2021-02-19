package com.iptiq.provider.service;

import org.springframework.stereotype.Service;

@Service
public class ProviderService {

	public String get(Integer port) {
		return "Provider-"+port;
	}
}
