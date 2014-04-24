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
        
        parser.check(args[1]);
    }
}
