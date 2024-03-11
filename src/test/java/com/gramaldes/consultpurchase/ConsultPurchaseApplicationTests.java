package com.gramaldes.consultpurchase;

import com.gramaldes.consultpurchase.exception.EmptyRatesException;
import com.gramaldes.consultpurchase.exception.PurchaseNotFoundException;
import com.gramaldes.consultpurchase.model.Purchase;
import com.gramaldes.consultpurchase.model.DTO.PurchaseDTO;
import com.gramaldes.consultpurchase.repository.PurchaseRepository;
import com.gramaldes.consultpurchase.service.PurchaseService;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ConsultPurchaseApplicationTests {

	@Autowired
	PurchaseService purchaseService;

	@Test
	@DisplayName("Should round successfully all Big Decimal values to only 2 digits in fractions")
	void roundingTest() {
		assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.5658468486186),2)).isEqualTo( BigDecimal.valueOf(1.57));
		assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.555),2)).isEqualTo( BigDecimal.valueOf(1.56));
		assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.565),2)).isEqualTo( BigDecimal.valueOf(1.56));
		assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.575),2)).isEqualTo( BigDecimal.valueOf(1.58));
	}

	@Test
	@DisplayName("Should successfully store a purchase and consult it and the latest exchange rate")
	void checkSuccessStoreAndConsult() throws Exception {
		Purchase mockedPurchase = new Purchase(UUID.randomUUID(),"Test 1",LocalDate.now(),BigDecimal.valueOf(100.80));
		mockedPurchase = purchaseService.save(mockedPurchase);
		PurchaseDTO testPurchaseDTO = purchaseService.findConvertedPurchaseById(mockedPurchase.getId(),"Euro Zone-Euro");
		BigDecimal checkConvertedAmount = (BigDecimal.valueOf(100.80).multiply(testPurchaseDTO.getExchangeRate()))
				.setScale(2,RoundingMode.HALF_EVEN);
		assertThat(testPurchaseDTO.getConvertedAmount().equals(checkConvertedAmount));
	}

	@Test
	@DisplayName("Should thrown Exception when there's no purchase stored in db with a wrong id")
	void failOnConsultPurchaseNotStored() throws PurchaseNotFoundException {
		Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(PurchaseNotFoundException.class, () -> {
			purchaseService.findConvertedPurchaseById(UUID.randomUUID(),"Euro Zone-Euro");
		});
		org.junit.jupiter.api.Assertions.assertEquals("Purchase not found", thrown.getMessage());
	}

	@Test
	@DisplayName("Should thrown Exception when there's no valid rate of exchange in the last 6 months or a wrong currency is used")
	void failOnConsultPurchaseOnWrongCurrency() throws EmptyRatesException {
		Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(EmptyRatesException.class, () -> {
			purchaseService.getLatestExchangeRate("test", LocalDate.now());
		});
		org.junit.jupiter.api.Assertions.assertEquals("No valid exchange rate available in the last 6 months for the specified currency: " +
				"test and date " + LocalDate.now(), thrown.getMessage());
	}

	@Test
	@DisplayName("Should successfully save a new purchase and store it in database")
	void successfullSavePurchaseCase() throws Exception {
		PurchaseDTO mockedPurchase = new PurchaseDTO("Mocked Purchase DTO",
				LocalDate.now(),
				BigDecimal.valueOf(225.46),
				BigDecimal.valueOf(4.85),
				BigDecimal.valueOf(1093.48));

		Purchase validEntity = new Purchase();
		validEntity.setAmount(BigDecimal.valueOf(225.46));
		validEntity.setDate(LocalDate.now());
		validEntity.setDesc("Test Purchase");

		validEntity = purchaseService.save(validEntity);
		assertThat(validEntity.getId() != null);
	}

	@Test
	@DisplayName("Should not save a purchase transactions with invalid fields")
	void failedSavePurchaseCase() throws Exception {
		Purchase invalidEntity = new Purchase(UUID.randomUUID(),"Test 1",LocalDate.now(),BigDecimal.valueOf(0.00));
        Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class, () -> {
			purchaseService.save(invalidEntity);
		});
		org.junit.jupiter.api.Assertions.assertEquals("save.purchase.amount: Minimum value is U$0.01", thrown.getMessage());
	}



}
