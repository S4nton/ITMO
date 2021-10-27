package expression;

public class Or extends AbstractExpression{
    public Or(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return a | b;
    }

    @Override
    protected String getSymbol() {
        return "|";
    }

    @Override
    public int getPriority() {
        return Priority.OrAndXor.ordinal();
    }

    @Override
    public int getType() {
        return 0;
    }
}
