package expression;

public interface NotAbstractExpression extends Expression, TripleExpression{
    int getType();
    int getPriority();
}
