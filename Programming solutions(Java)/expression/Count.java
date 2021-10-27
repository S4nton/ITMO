package expression;

public final class Count extends Unary {
    public Count(NotAbstractExpression num) {
        super(num);
    }

    public int operation(int num) {
        return Integer.bitCount(num);
    }

    @Override
    public String getSymbol() {
        return "count ";
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
