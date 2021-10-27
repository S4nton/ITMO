package expression.exceptions;

import expression.Subtract;
import expression.NotAbstractExpression;

public class CheckedSubtract extends Subtract{
    public CheckedSubtract(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    public int operation(int a, int b) {
        if (b > 0 && Integer.MIN_VALUE + b > a || b < 0 && Integer.MAX_VALUE + b < a) {
            throw new ExceptionOfOverflow();
        }
        return a - b;
    }
}
