package expression.exceptions;

import expression.Abs;
import expression.NotAbstractExpression;

public class CheckedAbs extends Abs{
    public CheckedAbs(NotAbstractExpression num) {
        super(num);
    }

    @Override
    public int operation(int a) {
        if (a == Integer.MIN_VALUE) {
            throw new ExceptionOfOverflow();
        }
        return (a < 0 ? -a : a);
    }
}
