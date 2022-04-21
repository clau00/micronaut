package com.learning.broker.wallet.error;

import com.learning.broker.api.RestApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomError implements RestApiResponse {

    private int status;
    private String error;
    private String message;
    private String path;
}
