package com.gramaldes.consultpurchase.service;

import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.CurrencyExchangeRateDTO;
import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.TreasuryResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
class TreasuryServiceTest {

    @Autowired
    TreasuryService treasuryService;


    @Test
    @DisplayName("Valid parameters should return a valid list of exchange rates sorted DESC by record_date")
    void getTreasuryResponseSuccessCase() throws ExecutionException, InterruptedException {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String maxDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        TreasuryResponseDTO treasuryResponseDTO = treasuryService.getTreasuryResponse(maxDate, date, "Brazil-Real");
        List<CurrencyExchangeRateDTO> listRates = treasuryResponseDTO.getData();
        Assert.isTrue(treasuryResponseDTO.getMeta().getCount() > 1, "There should be more than one valid result, check the API.");
        Assert.isTrue(listRates.get(0).getRecord_date().isAfter(listRates.get(1).getRecord_date()), "The order is wrong, check the API to see if the results match the test.");
    }

    @Test
    @DisplayName("Any invalid parameter should call the API and return an empty list of exchange rates")
    void getTreasuryResponseFailedCase() throws ExecutionException, InterruptedException {
        String date = LocalDate.now().toString();
        String maxDate = LocalDate.now().minusYears(1).toString();
        TreasuryResponseDTO treasuryResponseDTO = treasuryService.getTreasuryResponse(maxDate, date, "test");
        List<CurrencyExchangeRateDTO> listRates = treasuryResponseDTO.getData();
        Assert.isTrue(treasuryResponseDTO.getMeta().getCount() == 0, "It shouldn't return any valid results, confirm the values in the API.");
        Assert.isTrue(listRates.isEmpty(), "It shouldn't return any valid results, confirm the values in the API.");
    }
}