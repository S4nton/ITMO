package expression.exceptions;

import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        //System.out.println(Integer.MAX_VALUE);
        //System.out.println(Integer.MIN_VALUE);
        //System.out.println(0 - -2147483648);
        System.out.println(new ExpressionParser().parse("1000000*x*x*x*x*x/(x-1)").evaluate(5, 0, 0));
    }
}
