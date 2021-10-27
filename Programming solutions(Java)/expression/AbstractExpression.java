package expression;

public abstract class AbstractExpression implements NotAbstractExpression{
    private NotAbstractExpression a, b;
    abstract protected int operation(int a, int b);
    abstract protected String getSymbol();

    public AbstractExpression(NotAbstractExpression a, NotAbstractExpression b) {
        this.a = a;
        this.b = b;
    }

    public boolean equals(Object checkObj) {
        if (checkObj == null || checkObj.getClass() != this.getClass()) {
            return false;
        } else {
            AbstractExpression newClassObj = (AbstractExpression) checkObj;
            return this.a.equals(newClassObj.a) && this.b.equals(newClassObj.b);
        }
    }

    @Override
    public int evaluate(int num) {
        return operation(a.evaluate(num), b.evaluate(num));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operation(a.evaluate(x, y, z), b.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + a + " " + this.getSymbol() + " " + b + ")";
    }

    private String needBrackets (final boolean check, final NotAbstractExpression a) {
        if (check) {
            return '(' + a.toMiniString() + ')';
        } else {
            return a.toMiniString();
        }
    }

    @Override
    public String toMiniString() {
        return needBrackets(a.getPriority() > this.getPriority(), a) + " " +
                this.getSymbol() + " " + needBrackets(b.getPriority() > this.getPriority() || (b.getPriority() == this.getPriority() && b.getType() + this.getType() > 0), b);
    }

    @Override
    public int hashCode() {
        return ((7727 + a.hashCode()) * 7727 + b.hashCode()) * 7727 + this.getClass().hashCode();
    }
}
