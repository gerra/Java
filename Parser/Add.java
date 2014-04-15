public class Add extends BinaryOperation {
    public Add(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int f = first.evaluate(x, y, z);
        int s = second.evaluate(x, y, z);
        if (s  > 0 && f > Integer.MAX_VALUE - s || 
            s <= 0 && f < Integer.MIN_VALUE - s) {
            throw new OverflowException("overflow");
        }
        return f + s;
    }
}
