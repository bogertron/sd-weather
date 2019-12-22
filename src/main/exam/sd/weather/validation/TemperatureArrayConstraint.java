package exam.sd.weather.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TemperatureValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TemperatureArrayConstraint {
    String message() default "Temperature array can only have 23 entries, must be between [-200, 200] and " +
            "cannot have more than 1 decimal precision";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
