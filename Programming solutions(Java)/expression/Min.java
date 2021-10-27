package expression;

public class Min extends AbstractExpression{
    public Min(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return (a < b ? a : b);
    }

    @Override
    protected String getSymbol() {
        return " min ";
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
