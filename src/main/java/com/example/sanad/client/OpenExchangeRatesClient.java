package com.example.sanad.client;

import com.example.sanad.client.dto.ExchangeRatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openExchangeClient", url = "https://openexchangerates.org/api")
public interface OpenExchangeRatesClient {

    @GetMapping("/latest.json")
    ExchangeRatesResponse getLatestRates(@RequestParam("app_id") String appId);
}
