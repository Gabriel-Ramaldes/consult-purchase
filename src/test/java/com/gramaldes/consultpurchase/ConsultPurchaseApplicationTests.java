package com.gramaldes.consultpurchase;

import com.gramaldes.consultpurchase.exception.EmptyRatesException;
import com.gramaldes.consultpurchase.exception.PurchaseNotFoundException;
import com.gramaldes.consultpurchase.model.DTO.PurchaseDTO;
import com.gramaldes.consultpurchase.model.Purchase;
import com.gramaldes.consultpurchase.service.PurchaseService;
import jakarta.validation.ConstraintViolationException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class ConsultPurchaseApplicationTests {

    @Autowired
    PurchaseService purchaseService;

    @Test
    @DisplayName("Should round successfully all Big Decimal values to only 2 digits in fractions as expected")
    void roundingTest() {
        assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.5658468486186), 2)).isEqualTo(BigDecimal.valueOf(1.57));
        assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.555), 2)).isEqualTo(BigDecimal.valueOf(1.56));
        assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.565), 2)).isEqualTo(BigDecimal.valueOf(1.56));
        assertThat(purchaseService.roundToScale(BigDecimal.valueOf(1.575), 2)).isEqualTo(BigDecimal.valueOf(1.58));
    }

    @Test
    @DisplayName("Should successfully store a purchase and then convert the amount with the latest exchange rate")
    void checkSuccessStoreAndConsult() throws Exception {
        Purchase mockedPurchase = new Purchase(UUID.randomUUID(), "Test 1", LocalDate.now(), BigDecimal.valueOf(100.80));
        mockedPurchase = purchaseService.save(mockedPurchase);
        PurchaseDTO testPurchaseDTO = purchaseService.findConvertedPurchaseById(mockedPurchase.getId(), "Euro Zone-Euro");
        BigDecimal checkConvertedAmount = (BigDecimal.valueOf(100.80).multiply(testPurchaseDTO.getExchangeRate()))
                .setScale(2, RoundingMode.HALF_EVEN);

        Assert.isTrue(testPurchaseDTO.getConvertedAmount().equals(checkConvertedAmount), "The values are different.");
    }

    @Test
    @DisplayName("Should thrown an Exception when consulting the database with an unused id")
    void failOnConsultPurchaseNotStored() throws PurchaseNotFoundException {
        Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(PurchaseNotFoundException.class, () -> {
            purchaseService.findConvertedPurchaseById(UUID.randomUUID(), "Euro Zone-Euro");
        });
        assertEquals("Purchase not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Should thrown Exception when there's no valid rate of exchange in the last 6 months or a wrong currency is used")
    void failOnConsultPurchaseOnWrongCurrency() throws EmptyRatesException {
        Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(EmptyRatesException.class, () -> {
            purchaseService.getLatestExchangeRate("test", LocalDate.now());
        });
        assertEquals(
                "No valid exchange rate available in the last 6 months for the specified currency: " +
                        "test and date " + LocalDate.now(), thrown.getMessage());
    }

    @Test
    @DisplayName("Should successfully save a new purchase and persist it in the database")
    void successfullSavePurchaseCase() throws Exception {
        Purchase validEntity = new Purchase();
        validEntity.setAmount(BigDecimal.valueOf(225.46));
        validEntity.setDate(LocalDate.now());
        validEntity.setDesc("Test Purchase");

        validEntity = purchaseService.save(validEntity);
        Assert.notNull(validEntity, "The entity was not persisted");
    }

    @Test
    @DisplayName("Should not save a purchase transactions with invalid fields")
    void failedSavePurchaseCase() {
        Purchase invalidEntity = new Purchase(UUID.randomUUID(), "Test 1", LocalDate.now(), BigDecimal.valueOf(0.00));
        Exception thrown = org.junit.jupiter.api.Assertions.assertThrows(ConstraintViolationException.class,
                () -> purchaseService.save(invalidEntity));
        assertEquals("save.purchase.amount: Minimum value is U$0.01",
                thrown.getMessage());
    }

	@Test
	@DisplayName("Should successfully consult 5 purchases saved in the same day")
	void successfullFindPurchasesByDate() {
		List<Purchase> tests = new ArrayList<>();
		int count = 0;
		while(count < 5) {
			val testEntity = new Purchase();
			testEntity.setAmount(BigDecimal.valueOf(225.46));
			testEntity.setDate(LocalDate.now());
			testEntity.setDesc("Test " + count);
			tests.add(testEntity);
			count++;
		}
		tests.forEach(purchase -> {
			purchaseService.save(purchase);
		});
        tests = purchaseService.findAllByDateOrderById(Pageable.ofSize(5), LocalDate.now());

		Assert.isTrue(tests.size() == 5, "One or more purchases were not found, check the find by date function.");
		tests.forEach(purchase -> {
		  Assert.isTrue(Objects.equals(purchase.getDate(), LocalDate.now()), "One of the dates didn't match the test!");
		});
	}


}
