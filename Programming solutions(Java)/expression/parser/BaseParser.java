package expression.parser;

import expression.exceptions.ExceptionOfExpect;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser{
    public static final char END = '\0';
    private CharSource source;
    protected char ch = 0xffff;

    protected void setSource(final CharSource source) {
        this.source = source;
        nextChar();
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    protected void expect(final char c) {
        if (ch != c) {
            throw new ExceptionOfExpect("'" + c + "', found '" + ch + "'", getPosition());
        }
        nextChar();
    }

    protected void expect(final String value) {
        for (char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (!source.hasNext(i) || source.next(i) != expected.charAt(i)) {
                return false;
            }
        }
        expect(expected);
        return true;
    }

    protected boolean eof() {
        return test(END);
    }

    protected boolean isDigit() {
        return ch >= '0' && ch <= '9';
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    protected int getPosition() {
        return source.getPosition();
    }
}
