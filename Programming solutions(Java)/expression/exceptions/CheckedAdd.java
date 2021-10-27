package expression.exceptions;

import expression.Add;
import expression.NotAbstractExpression;

public class CheckedAdd extends Add {
    public CheckedAdd(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    public int operation(int a, int b) {
        //System.out.println(a);
        //System.out.println(b);
        if (b > 0 && Integer.MAX_VALUE - b < a || b < 0 && Integer.MIN_VALUE - b > a) {
            throw new ExceptionOfOverflow();
        }
        return a + b;
    }
}
