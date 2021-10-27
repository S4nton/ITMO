package expression.parser;

import expression.*;
import expression.exceptions.*;

import java.util.List;

public class ExpressionParser extends BaseParser implements Parser {
    private static final int maxPriority = 1, minPriority = -1;

    @Override
    public NotAbstractExpression parse(String expression) {
        setSource(new StringSource(expression));
        skipWhitespace();
        NotAbstractExpression parsingExpression = parse(maxPriority);
        if (ch != '\0') {
            throw new ExceptionOfUnexpectedSymbol(ch, getPosition());
        }
        return parsingExpression;
    }

    private NotAbstractExpression parse(int priorityExpression) {
        skipWhitespace();
        if (priorityExpression == minPriority) {
            return getExpressionWithMinPriority();
        }
        NotAbstractExpression parsing = parse(priorityExpression - 1);
        while (true) {
            skipWhitespace();
            int us = 0;
            for (String str : getOperationWithPriority(priorityExpression)) {
                if (test(str)) {
                    parsing = getExpression(str, parsing, parse(priorityExpression - 1));
                    us = 1;
                    break;
                }
            }
            if (us == 0) {
                break;
            }
        }
        //System.out.println(priorityExpression);
        //System.out.println(parsing.toMiniString() + priorityExpression);
        skipWhitespace();
        return parsing;
    }

    private List<String> getOperationWithPriority(final int p) {
        if (p == 0) {
            return List.of("*", "/");
        } else {
            return List.of("+", "-");
        }
    }

    private boolean checkSymbolAfterAbsAndSqrt(final String str) {
        for (int i = 0; i < str.length(); i++) {
            if (ch == str.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    private NotAbstractExpression getExpressionWithMinPriority() {
        if (test('-')) {
            skipWhitespace();
            if (isDigit()) {
                return constPareser(true);
            } else {
                return new CheckedNegate(getExpressionWithMinPriority());
            }
        } else if (isDigit()) {
            return constPareser(false);
        } else if (test('(')) {
            NotAbstractExpression res = parse(maxPriority);
            expect(')');
            return res;
        } else if (ch == 'x' || ch == 'y' || ch == 'z') {
            String var = Character.toString(ch);
            nextChar();
            return new Variable(var);
        } else {
            throw new ExceptionOfInvalidVariable(ch, getPosition());
        }
    }

    private AbstractExpression getExpression(String nowOperation, NotAbstractExpression a, NotAbstractExpression b) {
        switch (nowOperation) {
            case "*":
                return new CheckedMultiply(a, b);
            case "/":
                return new CheckedDivide(a, b);
            case "+":
                return new CheckedAdd(a, b);
            case "-":
                return new CheckedSubtract(a, b);
            default:
                throw new ExceptionOfInvalidOperation("Invalid operation " + nowOperation, getPosition());
        }
    }

    private Const constPareser(boolean checkMinus) {
        StringBuilder sb = new StringBuilder();
        if (checkMinus)
            sb.append('-');
        while (isDigit()) {
            sb.append(ch);
            nextChar();
        }
        skipWhitespace();
        try {
            return new Const(Integer.parseInt(sb.toString()));
        } catch (NumberFormatException e) {
            throw new ExceptionOfInvalidConst(sb.toString(), getPosition());
        }
    }
}