package expression;

public class Max extends AbstractExpression{
    public Max(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return (a < b ? b : a);
    }

    @Override
    protected String getSymbol() {
        return " max ";
    }

    @Override
    public int getPriority() {
        return Priority.MinOrMax.ordinal();
    }

    @Override
    public int getType() {
        return 0;
    }
}
