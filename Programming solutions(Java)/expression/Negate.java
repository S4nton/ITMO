package expression;

public class Negate extends Unary {
    public Negate(NotAbstractExpression num) {
        super(num);
    }

    public int operation(int num) {
        return -num;
    }

    @Override
    public String getSymbol() {
        return "-";
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getPriority() {
        return Priority.MultiplyOrDivide.ordinal();
    }
}
