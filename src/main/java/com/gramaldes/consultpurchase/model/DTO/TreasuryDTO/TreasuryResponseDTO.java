package com.gramaldes.consultpurchase.model.DTO.TreasuryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TreasuryResponseDTO {
    private List<CurrencyExchangeRateDTO> data;
    private MetaDTO meta;
}
