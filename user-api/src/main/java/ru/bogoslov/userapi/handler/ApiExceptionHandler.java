package ru.bogoslov.userapi.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.bogoslov.userapi.exception.BusinessException;
import ru.bogoslov.userapi.model.ErrorEntity;

@Slf4j
@RestControllerAdvice("ru.bogoslov.userapi.controller")
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> handleTechExceptions(BusinessException exception) {
        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .internalServerError()
                .body(new ErrorEntity(exception.getMessage(), -10000));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorEntity> handleBusinessExceptions(BusinessException exception) {
        log.warn(exception.getMessage(), exception);

        return ResponseEntity
                .badRequest()
                .body(new ErrorEntity(exception.getMessage(), -20000));
    }
}
