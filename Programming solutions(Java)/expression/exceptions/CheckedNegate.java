package expression.exceptions;

import expression.Negate;
import expression.NotAbstractExpression;

public class CheckedNegate extends Negate{
    public CheckedNegate(NotAbstractExpression a) {
        super(a);
    }

    @Override
    public int operation(int a) {
        if (a > 0 && Integer.MIN_VALUE + a > 0 || a < 0 && Integer.MAX_VALUE + a < 0) {
            throw new ExceptionOfOverflow();
        }
        return -a;
    }
}
