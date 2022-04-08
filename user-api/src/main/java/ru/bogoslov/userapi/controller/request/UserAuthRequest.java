package ru.bogoslov.userapi.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserAuthRequest {

    @NotEmpty(message = "Invalid login value")
    @ApiModelProperty(name = "User login", required = true)
    private String login;

    @NotEmpty(message = "Invalid password value")
    @ApiModelProperty(name = "User password", required = true)
    private String password;
}
