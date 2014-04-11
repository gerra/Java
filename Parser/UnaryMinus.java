public class UnaryMinus extends UnaryOperation {
    UnaryMinus(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) {
        return -exp.evaluate(x, y, z);
    }
}
