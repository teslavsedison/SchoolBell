package org.bell.framework;

import javafx.scene.control.Control;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.Validator;

public class IntegerRangeValidator implements Validator<String> {

    private int firstVal;
    private int secondVal;

    public IntegerRangeValidator(int firstVal, int secondVal) {
        this.firstVal = firstVal;
        this.secondVal = secondVal;
    }

    @Override
    public ValidationResult apply(Control control, String s) {
        boolean res = false;
        try {
            res = Integer.parseInt(s) >= firstVal && Integer.parseInt(s) <= secondVal;
        } catch (NumberFormatException e) {
            return ValidationResult.fromError(control, "Giriş değerleri sayı değil.");
        }

        if (res)
            return null;
        return ValidationResult.fromError(control, String.format("Verilen değerler %1d-%2d bu aralıkta olmalıdır", firstVal, secondVal));
    }
}
