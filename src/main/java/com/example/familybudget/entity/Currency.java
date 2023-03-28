package com.example.familybudget.entity;

import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public enum Currency {

    RUB_RUS("Российский рубль"),
    RUB_BEL("Белорусский рубль"),
    TENGE_KAZ("Казахстанский тенге"),
    DOLLAR_USA("Доллар США"),
    EURO_EU("Евро");

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
