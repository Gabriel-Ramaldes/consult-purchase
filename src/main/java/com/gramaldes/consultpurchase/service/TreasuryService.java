package com.gramaldes.consultpurchase.service;

import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.TreasuryResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutionException;

@Service
public class TreasuryService {
    private final WebClient webClient;

    public TreasuryService(WebClient.Builder webClientBuilder) {
        String baseUrl = "https://api.fiscaldata.treasury.gov/services/api/fiscal_service/";
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public TreasuryResponseDTO getTreasuryResponse(String maxDate, String date, String currency) throws ExecutionException, InterruptedException {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("v1/accounting/od/rates_of_exchange")
                        .queryParam("fields", "country_currency_desc,exchange_rate,record_date")
                        .queryParam("filter", "record_date:gte:{maxDate},record_date:lte:{date},country_currency_desc:in:({currency})")
                        .queryParam("sort", "-record_date")
                        .build(maxDate, date, currency))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(TreasuryResponseDTO.class)
                .toFuture()
                .get().getBody();
    }

}
