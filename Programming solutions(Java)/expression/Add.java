package expression;

public class Add extends AbstractExpression{
    public Add(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return a + b;
    }

    @Override
    protected String getSymbol() {
        return "+";
    }

    @Override
    public int getPriority() {
        return Priority.SubtractOrAdd.ordinal();
    }

    @Override
    public int getType() {
        return 0;
    }
}
