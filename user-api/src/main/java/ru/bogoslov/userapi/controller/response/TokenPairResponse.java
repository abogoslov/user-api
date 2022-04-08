package ru.bogoslov.userapi.controller.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TokenPairResponse {

    @ApiModelProperty(name = "Short exp JWT")
    private final String accessToken;

    @ApiModelProperty(name = "Long exp JWT")
    private final String refreshToken;
}
