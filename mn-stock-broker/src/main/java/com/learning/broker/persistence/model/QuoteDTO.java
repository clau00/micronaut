package com.learning.broker.persistence.model;

import io.micronaut.core.annotation.Introspected;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Introspected
public class QuoteDTO {

    private Integer id;
    private BigDecimal volume;

}
