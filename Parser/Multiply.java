public class Multiply extends BinaryOperation {
    public Multiply(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int f = first.evaluate(x, y, z);
        int s = second.evaluate(x, y, z);
        if (s == 0) {
            return 0;
        }
        
        double check = (double)f * (double)s;
        if (check > Integer.MAX_VALUE || check < Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
        
        return (int)check;
    }
}
