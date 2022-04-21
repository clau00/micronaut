package com.learning.broker;

import com.learning.broker.api.RestApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Quote implements RestApiResponse {

    private Symbol symbol;
    private BigDecimal bid;
    private BigDecimal ask;
    private BigDecimal lastPrice;
    private BigDecimal volume;
}
