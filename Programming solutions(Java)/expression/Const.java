package expression;

public class Const implements NotAbstractExpression{
    private Number num;
    public Const(Number num) {
        this.num = num;
    }

    @Override
    public int evaluate(int num) {
        return this.num.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.num.intValue();
    }

    @Override
    public boolean equals(Object checkObj) {
        if (checkObj == null || checkObj.getClass() != Const.class || !(this.toMiniString().equals(((Const) checkObj).toMiniString()))) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }

    @Override
    public int hashCode() {

        return this.num.intValue();
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
