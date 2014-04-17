public class Abs extends UnaryOperation {
    Abs(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int e = exp.evaluate(x, y, z);
        double res = (e < 0 ? -(double)e : (double)e);
        if (res < Integer.MIN_VALUE || res > Integer.MAX_VALUE) {
            throw new OverflowException("overflow");
        }
        return (int)res;
    }
}
