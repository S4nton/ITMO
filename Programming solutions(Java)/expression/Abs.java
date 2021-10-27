package expression;

public class Abs extends Unary {
    public Abs(NotAbstractExpression num) {
        super(num);
    }

    public int operation(int num) {
        return (num < 0 ? -num : num);
    }

    @Override
    public String getSymbol() {
        return "abs";
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
