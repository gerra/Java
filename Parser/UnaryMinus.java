public class UnaryMinus extends UnaryOperation {
    UnaryMinus(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException  {
        int e = exp.evaluate(x, y, z);
        double res = -(double)e;
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return (int)res;
    }
}
