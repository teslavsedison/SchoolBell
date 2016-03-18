package org.bell.framework;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

public class TimeHHMMValidator implements Validator<String> {


    private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    @Override
    public ValidationResult apply(Control control, String s) {
        boolean matches = ((TextField) control).getText().matches(TIME24HOURS_PATTERN);
        if (!matches)
            return ValidationResult.fromErrorIf(control, "Hatalı saat biçimi.", !matches);
        return null;
    }
}
