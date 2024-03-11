package com.gramaldes.consultpurchase.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class Violation {
    private final String fieldName;
    private final String message;
}
