package expression.exceptions;

import expression.Divide;
import expression.NotAbstractExpression;

public class CheckedDivide extends Divide{
    public CheckedDivide(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    public int operation(int a, int b) {
        if (b == 0) {
            throw new ExceptionOfDivideByZero();
        } else if (a == Integer.MIN_VALUE && b == -1) {
            throw new ExceptionOfOverflow();
        }
        return a / b;
    }
}
