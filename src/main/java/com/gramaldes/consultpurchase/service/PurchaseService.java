package com.gramaldes.consultpurchase.service;

import com.gramaldes.consultpurchase.exception.EmptyRatesException;
import com.gramaldes.consultpurchase.exception.PurchaseNotFoundException;
import com.gramaldes.consultpurchase.model.DTO.PurchaseDTO;
import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.CurrencyExchangeRateDTO;
import com.gramaldes.consultpurchase.model.DTO.TreasuryDTO.TreasuryResponseDTO;
import com.gramaldes.consultpurchase.model.Purchase;
import com.gramaldes.consultpurchase.repository.PurchaseRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@Validated
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private TreasuryService treasuryService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Purchase save(@Valid Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

   public List<Purchase> findAllByDateOrderById(Pageable pageable, LocalDate date) {
        return purchaseRepository.findAllByDateOrderById(date, pageable);
   }

    public PurchaseDTO findConvertedPurchaseById(UUID id, String currency) throws Exception {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException("Purchase not found"));
        return getConvertedPurchase(purchase, currency);
    }


    public PurchaseDTO getConvertedPurchase(Purchase purchase, String currency) throws ExecutionException, InterruptedException {
        CurrencyExchangeRateDTO currencyExchange = getLatestExchangeRate(currency, purchase.getDate());
        return new PurchaseDTO(
                purchase.getId(),
                purchase.getDesc(),
                purchase.getDate(),
                purchase.getAmount(),
                currencyExchange.getExchange_rate(),
                this.roundToScale((currencyExchange.getExchange_rate().multiply(purchase.getAmount())), 2)
        );
    }

    public CurrencyExchangeRateDTO getLatestExchangeRate(String currency, LocalDate date) throws ExecutionException, InterruptedException {
        LocalDate maxDate = date.minusMonths(6);
        TreasuryResponseDTO response = treasuryService.getTreasuryResponse(maxDate.format(formatter), date.format(formatter), currency);
        List<CurrencyExchangeRateDTO> listRates = Objects.requireNonNull(response.getData());
        if (listRates.isEmpty()) {
            throw new EmptyRatesException("No valid exchange rate available in the last 6 months for the specified currency: " +
                    currency + " and date " + date);
        }
        return listRates.get(0);
    }


    public BigDecimal roundToScale(BigDecimal value, Integer scale) {
        return value.setScale(scale, RoundingMode.HALF_EVEN);
    }
}
