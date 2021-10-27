package expression;

public class Subtract extends AbstractExpression{
    public Subtract(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return a - b;
    }

    @Override
    protected String getSymbol() {
        return "-";
    }

    @Override
    public int getPriority() {
        return Priority.SubtractOrAdd.ordinal();
    }

    @Override
    public int getType() {
        return 1;
    }
}
