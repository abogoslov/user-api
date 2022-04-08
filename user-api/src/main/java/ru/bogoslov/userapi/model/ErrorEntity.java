package ru.bogoslov.userapi.model;

import io.swagger.annotations.ApiResponse;
import lombok.Data;

@Data
public class ErrorEntity {

    private final String message;
    private final Integer code;
}
