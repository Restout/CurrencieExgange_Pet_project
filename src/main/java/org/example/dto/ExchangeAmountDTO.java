package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.ExchangeRate;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeAmountDTO {
    ExchangeRate exchangeRate;
    BigDecimal amount;
    BigDecimal convertedAmount;
}
