package expression.exceptions;

import expression.Multiply;
import expression.NotAbstractExpression;

public class CheckedMultiply extends Multiply{
    public CheckedMultiply(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    public int operation(int a, int b) {
        int multiply = a * b;
        if (a != 0 && b != 0 && (a < 0 && b < 0 && Integer.MAX_VALUE / b > a || a > 0 && b > 0 && Integer.MAX_VALUE / b < a ||
                a > 0 && b < 0 && Integer.MIN_VALUE / a > b || a < 0 && b > 0 && Integer.MIN_VALUE / b > a)
        ) {
            throw new ExceptionOfOverflow();
        }
        return a * b;
    }
}
