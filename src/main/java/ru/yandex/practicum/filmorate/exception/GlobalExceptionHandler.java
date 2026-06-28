package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ValidationErrorResponse body = ValidationErrorResponse.builder()
                .message("Ошибка валидации")
                .errors(errors)
                .build();
        log.warn("Ошибка валидации: {}", errors);
        log.debug("Ошибка валидации", ex);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleWrongRequestBody(HttpMessageNotReadableException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message("Ошибка парсинга тела запроса")
                .details(ex.getMessage())
                .build();
        log.warn("Ошибка парсинга тела запроса: {}", ex.getMessage());
        log.debug("Ошибка парсинга тела запроса", ex);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleAllErrors(NoResourceFoundException ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message("Ресурс не найден")
                .details(ex.getMessage())
                .build();
        log.warn("Ресурс не найден: {}", ex.getMessage());
        log.info("Ресурс не найден. Метод: {}. Путь: {}", ex.getHttpMethod(), ex.getResourcePath());
        log.debug("Ресурс не найден", ex);
        return ResponseEntity.internalServerError().body(body);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllErrors(Exception ex) {
        ErrorResponse body = ErrorResponse.builder()
                .message("Внутренняя ошибка сервера")
                .details(ex.getMessage())
                .build();
        log.error("Внутренняя ошибка сервера: {}", ex.getMessage());
        log.debug("Внутренняя ошибка сервера:", ex);
        return ResponseEntity.internalServerError().body(body);
    }
}
