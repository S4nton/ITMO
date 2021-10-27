package expression;

public class Variable implements NotAbstractExpression{
    private String str;

    public Variable(String str) {
        this.str = str;
    }

    @Override
    public int evaluate(int num) {
        return num;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (str) {
            case "x":
                return x;
            case "y":
                return y;
            default:
                return z;
        }
    }

    @Override
    public String toString() {
        return this.str;
    }

    public int hashCode() {
        return str.hashCode();
    }

    @Override
    public boolean equals(Object checkObj) {
        return !(checkObj == null || checkObj.getClass() != Variable.class || !(str.equals(((Variable) checkObj).toString())));
    }

    @Override
    public int getPriority() {
        return Priority.ConOrVar.ordinal();
    }

    @Override
    public int getType() {
        return 0;
    }
}
