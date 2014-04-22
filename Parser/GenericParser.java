import java.math.BigInteger;
public class GenericParser {
    
    private static ExpressionParser<? extends Number> parser;
    
    public static void main(String args[]) {
        if (args[0].equals("-bi")) {
            parser = new ExpressionParser<BigInteger>(new BigIntegerArithmetic());
        } else if (args[0].equals("-i")) {
            parser = new ExpressionParser<Integer>(new IntegerArithmetic());
        } else {
            parser = new ExpressionParser<Double>(new DoubleArithmetic());
        }
        
        for (int x = -100; x <= 100; x++) {
                for (int y = -100; y <= 100; y++) {
                    try {
                        System.out.println(parser.evaluate(args[1], "" + x, "" + y, "3"));
                    } catch(ParserException e) {
                        System.out.println(e.getMessage());
                    } catch (MyCalcException e) {
                        System.out.println("error");
                    }
                }
        }
    }
}
