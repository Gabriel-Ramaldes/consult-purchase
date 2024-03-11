package com.gramaldes.consultpurchase.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "purchase")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class Purchase implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    @Size(max=50, message = "Description must be at max 50 characters.")
    private String desc;

    @Column(nullable = false)
    @NotNull(message = "Date must not be null.")
    private LocalDate date;

    @Column(nullable = false, precision = 12, scale = 2)
    @DecimalMin(value = "0.01", message = "Minimum value is U$0.01")
    @Digits(integer = 12, fraction = 2)
    private BigDecimal amount;
}
