package expression.exceptions;

public class ExceptionOfSqrt extends ExceptionOfExpression{
    public ExceptionOfSqrt() {
        super("Trying to sqrt a negative number");
    }
}
