package expression.exceptions;

public class ExceptionOfInvalidOperation extends ExceptionOfParsing{
    public ExceptionOfInvalidOperation(final String str, final int pos) {
        super(str, pos);
    }
}
