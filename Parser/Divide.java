public class Divide extends BinaryOperation {
    public Divide(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int divident = second.evaluate(x, y, z);
        if (divident == 0) {
            throw new DBZException("division by zero");
        }
        int f = first.evaluate(x, y, z);
        double res = (double)f / (double)divident;
        if (res < Integer.MIN_VALUE || res > Integer.MAX_VALUE) {
            throw new OverflowException("overflow");
        }
        return (int)res;
    }
}
