package expression.exceptions;

public class ExceptionOfParsing extends RuntimeException{
    public ExceptionOfParsing(final String str, final int pos) {
        super(str + ". position: " + pos);
    }
}
