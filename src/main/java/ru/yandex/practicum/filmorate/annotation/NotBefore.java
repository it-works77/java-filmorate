package ru.yandex.practicum.filmorate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.yandex.practicum.filmorate.validator.NotBeforeValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBeforeValidator.class)
@Documented
public @interface NotBefore {
    String message() default "Дата не должна быть раньше {value}";
    String value();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
