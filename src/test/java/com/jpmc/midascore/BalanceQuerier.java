package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Balance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class BalanceQuerier {
    private static final Logger log = LoggerFactory.getLogger(BalanceQuerier.class);
    private final RestTemplate restTemplate;

    public BalanceQuerier(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Balance query(Long userId) {
        String url = "http://localhost:33400/balance?userId=" + userId;
        try {
            return restTemplate.getForObject(url, Balance.class);
        } catch (RestClientException e) {
            log.error("Failed to fetch balance from Incentive API for userId: {}", userId, e);
            // Fallback: Returns a default empty or zeroed balance rather than throwing a raw exception
            return new Balance(0.0); 
        }
    }
}
