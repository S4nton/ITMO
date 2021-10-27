package expression.exceptions;

public class ExceptionOfUnexpectedSymbol extends ExceptionOfParsing{
    public ExceptionOfUnexpectedSymbol(final char c, final int pos) {
        super("Unexpected symbols " + c, pos);
    }
}
