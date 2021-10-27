package expression;

public class Sqrt extends Unary {
    public Sqrt(NotAbstractExpression num) {
        super(num);
    }

    public int operation(int num) {
        return (int)Math.sqrt(num);
    }

    @Override
    public String getSymbol() {
        return "sqrt";
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
