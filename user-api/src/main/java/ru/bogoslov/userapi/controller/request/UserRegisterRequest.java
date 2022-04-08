package ru.bogoslov.userapi.controller.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserRegisterRequest {

    @NotEmpty(message = "Invalid login value")
    @ApiModelProperty(name = "User login", required = true)
    private String login;

    @NotEmpty(message = "Invalid password value")
    @ApiModelProperty(name = "User password", required = true)
    private String password;

    @NotEmpty(message = "Invalid password confirmation value")
    @ApiModelProperty(name = "User password confirmation", required = true)
    private String passwordConfirmation;

    @NotEmpty(message = "Invalid firstName value")
    @ApiModelProperty(name = "User first name")
    private String firstName;

    @NotEmpty(message = "Invalid lastName value")
    @ApiModelProperty(name = "User last name")
    private String lastName;

    @NotEmpty(message = "Invalid role value")
    @ApiModelProperty(name = "User role", required = true, allowableValues = "user, admin, courier")
    private String role;
}
