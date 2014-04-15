public class Log2 extends UnaryOperation {
    Log2(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int res = exp.evaluate(x, y, z);
        if (res < 0) {
            throw new LogNegException("log of negative expression");
        }
        if (res == 0) {
            throw new LogNegException("log of nil expression");
        }
        res = (int)(Math.log((double)res) / Math.log(2.0));
        return res;
    }
}
