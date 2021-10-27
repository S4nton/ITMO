package expression;

public class Xor extends AbstractExpression{
    public Xor(NotAbstractExpression a, NotAbstractExpression b) {
        super(a, b);
    }

    @Override
    protected int operation(int a, int b) {
        return a ^ b;
    }

    @Override
    protected String getSymbol() {
        return "^";
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
