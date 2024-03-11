package com.gramaldes.consultpurchase.service;

import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.CurrencyExchangeRateDTO;
import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.TreasuryResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(treasuryResponseDTO.getMeta().getCount() > 0);
        assertThat(listRates.get(0).getRecord_date().isAfter(listRates.get(1).getRecord_date()));
    }

    @Test
    @DisplayName("Any invalid parameter should call the API and return an empty list of exchange rates")
    void getTreasuryResponseFailedCase() throws ExecutionException, InterruptedException {
        String date = LocalDate.now().toString();
        String maxDate = LocalDate.now().minusYears(1).toString();
        TreasuryResponseDTO treasuryResponseDTO = treasuryService.getTreasuryResponse(maxDate, date, "test");
        List<CurrencyExchangeRateDTO> listRates = treasuryResponseDTO.getData();
        assertThat(treasuryResponseDTO.getMeta().getCount() == 0);
        assertThat(listRates.isEmpty());
    }
}