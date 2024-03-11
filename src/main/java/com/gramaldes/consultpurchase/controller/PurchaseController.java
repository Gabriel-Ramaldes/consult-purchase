package com.gramaldes.consultpurchase.controller;

import com.gramaldes.consultpurchase.model.Purchase;
import com.gramaldes.consultpurchase.model.DTO.PurchaseDTO;
import com.gramaldes.consultpurchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/purchase")
@Validated
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public Purchase save(@Valid @RequestBody Purchase purchase) {
        return purchaseService.save(purchase);
    }

    @GetMapping("/consult/{id}/{currency}")
    @ResponseStatus(HttpStatus.OK)
    @Validated
    public PurchaseDTO findPurchaseById(@RequestParam("id") UUID id, @RequestParam("currency") String currency) throws Exception {
        return purchaseService.findConvertedPurchaseById(id, currency);
    }
}
