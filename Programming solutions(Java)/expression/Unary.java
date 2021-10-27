package expression;

public abstract class Unary implements NotAbstractExpression {
    protected final NotAbstractExpression num;

    public Unary(NotAbstractExpression num) {
        this.num = num;
    }

    protected abstract int operation(int x);

    public abstract String getSymbol();

    @Override
    public int evaluate(int x) {
        return operation(num.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operation(num.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getSymbol() + "(" + num.toString() + ")";
    }

    @Override
    public String toMiniString() {
        boolean checkBrackets = num instanceof AbstractExpression;
        return getSymbol() +
                (checkBrackets ? "(" : "") +
                num.toMiniString() +
                (checkBrackets ? ")" : "");
    }
}
