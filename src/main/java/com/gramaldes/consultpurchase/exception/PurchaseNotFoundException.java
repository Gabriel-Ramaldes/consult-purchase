package com.gramaldes.consultpurchase.exception;

public class PurchaseNotFoundException extends RuntimeException {

    public PurchaseNotFoundException() {
        super("Purchase was not found");
    }

    public PurchaseNotFoundException(String message) {
        super(message);
    }
}
