package com.expense_manager.comman;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

@Converter(autoApply = true)
public class MoneyAttributeConverter implements AttributeConverter<Money, String> {

    @Override
    public String convertToDatabaseColumn(Money attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCurrencyUnit().getCode() + " " + attribute.getAmount().toString();
    }

    @Override
    public Money convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] parts = dbData.split(" ");
        return Money.of(CurrencyUnit.of(parts[0]), new java.math.BigDecimal(parts[1]));
    }
}
