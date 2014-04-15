public class Abs extends UnaryOperation {
    Abs(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int res = exp.evaluate(x, y, z);
        if (res == Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return (res < 0 ? -res : res);
    }
}
