public class Abs extends UnaryOperation {
    Abs(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) {
        int res = exp.evaluate(x, y, z);
        return (res < 0 ? -res : res);
    }
}
