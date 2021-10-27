package expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CharSource {
    boolean hasNext();
    boolean hasNext(int x);
    char next();
    char next(int x);
    int getPosition();
}
