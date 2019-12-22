package exam.sd.weather.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

/**
 * Handle validation logic for temperature array
 */
public class TemperatureValidator implements ConstraintValidator<TemperatureArrayConstraint, BigDecimal[]> {

    // for reference of documented ranges: https://www.space.com/17816-earth-temperature.html
    // set limits to next 100 just in case
    private static final BigDecimal MAX_TEMPERATURE = new BigDecimal(200);
    private static final BigDecimal MIN_TEMPERATURE = new BigDecimal(-200);

    @Override
    public boolean isValid(BigDecimal[] s, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        if (s == null || s.length != 24) {
            result = false;
        } else {
            for (BigDecimal dec : s) {
                if (dec.scale() > 1) {
                    result = false;
                } else if (dec.compareTo(MAX_TEMPERATURE) == 1) {
                    result = false;
                } else if (dec.compareTo(MIN_TEMPERATURE) == -1) {
                    result = false;
                }
            }
        }
        return result;
    }
}
