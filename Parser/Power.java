public class Power extends BinaryOperation {
    Power(Expression3 first, Expression3 second) {
        super(first, second);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int f = first.evaluate(x, y, z);
        int s = second.evaluate(x, y, z);
    
        if (s < 0) {
            throw new NegativePower("negative power");
        }
    
        double res = Math.pow((double)f, (double)s);
        
        if (res > (double)Integer.MAX_VALUE || res < (double)Integer.MIN_VALUE) {
            throw new OverflowException("overflow");
        }
         
        return (int)res;
    }
}
