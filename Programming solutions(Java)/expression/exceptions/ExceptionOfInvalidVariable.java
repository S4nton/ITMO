package expression.exceptions;

public class ExceptionOfInvalidVariable extends ExceptionOfParsing{
    public ExceptionOfInvalidVariable(final char str, final int pos) {
        super("Variable with incorrect name \"" + str + "\"", pos);
    }
}
