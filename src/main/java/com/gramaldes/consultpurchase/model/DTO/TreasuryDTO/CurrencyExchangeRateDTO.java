package com.gramaldes.consultpurchase.model.DTO.TreasuryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyExchangeRateDTO {

     private String country_currency_desc;
     private BigDecimal exchange_rate;
     private LocalDate record_date;
//    "country_currency_desc": "Brazil-Real",
//            "exchange_rate": "4.852",
//            "record_date"
}
