package com.gramaldes.consultpurchase.controller;

import com.gramaldes.consultpurchase.exception.PurchaseNotFoundException;
import com.gramaldes.consultpurchase.model.Purchase;
import com.gramaldes.consultpurchase.model.DTO.PurchaseDTO;
import com.gramaldes.consultpurchase.service.PurchaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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

    @GetMapping("/id={id}&currency={currency}")
    @ResponseStatus(HttpStatus.OK)
    @Validated
    public PurchaseDTO findPurchaseById(@RequestParam("id") UUID id, @RequestParam("currency") String currency) throws Exception {
        return purchaseService.findConvertedPurchaseById(id, currency);
    }
}
