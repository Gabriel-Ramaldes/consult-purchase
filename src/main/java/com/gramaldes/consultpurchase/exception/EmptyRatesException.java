package com.gramaldes.consultpurchase.exception;

public class EmptyRatesException extends RuntimeException {
    public EmptyRatesException(String message) {
        super(message);
    }

    public EmptyRatesException() {
        super("No valid Exchange Rate found in the last 6 months for the currency and date provided.");
    }
}
