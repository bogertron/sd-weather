package exam.sd.weather.validation;

import exam.sd.weather.validation.TemperatureValidator;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TemperatureValidatorTest {

    @Test
    public void testIncorrect_entries() {
        TemperatureValidator validator = new TemperatureValidator();
        BigDecimal[] shortCount = new BigDecimal[22];
        for (int idx = 0; idx < shortCount.length; idx++) {
            shortCount[idx] = new BigDecimal(22);
        }

        assertFalse(validator.isValid(shortCount, null));
    }

    @Test
    public void testIncorrect_ExtremeValue() {
        TemperatureValidator validator = new TemperatureValidator();
        BigDecimal[] shortCount = new BigDecimal[24];
        for (int idx = 0; idx < shortCount.length; idx++) {
            shortCount[idx] = new BigDecimal(22);
        }

        shortCount[10] = new BigDecimal(4000);

        assertFalse(validator.isValid(shortCount, null));
    }

    @Test
    public void testIncorrect_InvalidPrecision() {
        TemperatureValidator validator = new TemperatureValidator();
        BigDecimal[] shortCount = new BigDecimal[24];
        for (int idx = 0; idx < shortCount.length; idx++) {
            shortCount[idx] = new BigDecimal(22);
        }

        shortCount[10] = new BigDecimal("22.12345");

        assertFalse(validator.isValid(shortCount, null));
    }

    @Test
    public void testValidArray() {
        TemperatureValidator validator = new TemperatureValidator();
        BigDecimal[] shortCount = new BigDecimal[24];
        for (int idx = 0; idx < shortCount.length; idx++) {
            shortCount[idx] = new BigDecimal(22);
        }

        assertTrue(validator.isValid(shortCount, null));
    }
}
