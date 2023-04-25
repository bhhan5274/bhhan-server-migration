package io.github.bhhan.server.service.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeRangeValidator.class)
@Documented
public @interface TimeRange {
    String message() default "TimeRange is Invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


