package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ExchangeRate {
    @JsonProperty
    int id;
    @JsonProperty

    BigDecimal rate;
    @JsonProperty

    Currency baseCurrency;
    @JsonProperty

    Currency targetCurrency;
}
