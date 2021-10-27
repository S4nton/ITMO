package expression.exceptions;

public class ExceptionOfInvalidConst extends ExceptionOfParsing{
    public ExceptionOfInvalidConst(final String str, final int pos) {
        super("Overflow const " + str, pos);
    }
}
