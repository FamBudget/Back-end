package com.example.familybudget.entity;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public enum Currency {

    RUB("Российский рубль"),
    BYN("Белорусский рубль"),
    KZT("Казахстанский тенге"),
    USD("Доллар США"),
    EUR("Евро");

    private final String title;

    public static Optional<Currency> valid(String currencyString) {
        for (Currency currency: values()) {
            if (currency.name().equalsIgnoreCase(currencyString)) {
                return Optional.of(currency);
            }
        }
        return Optional.empty();
    }
}
