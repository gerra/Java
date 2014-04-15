public class Add extends BinaryOperation {
    public Add(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int f = first.evaluate(x, y, z);
        int s = second.evaluate(x, y, z);
        double res = (double)f + (double)s;
        if (res > Integer.MAX_VALUE - s || res < Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        return (int)res;
    }
}
