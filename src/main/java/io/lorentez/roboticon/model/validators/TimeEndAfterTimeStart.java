package io.lorentez.roboticon.model.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeEndAfterTimeStartValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeEndAfterTimeStart {
    String message() default "{lorentez.validation.constraints.TimeEndAfterTimeStart.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
