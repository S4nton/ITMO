package expression.exceptions;

import expression.Sqrt;
import expression.NotAbstractExpression;

public class CheckedSqrt extends Sqrt{
    public CheckedSqrt(NotAbstractExpression num) {
        super(num);
    }

    @Override
    public int operation(int a) {
        if (a < 0) {
            throw new ExceptionOfSqrt();
        }
        return (int)Math.sqrt(a);
    }
}
