public class Not extends UnaryOperation {
    Not(Expression3 exp) {
        super(exp);
    }
    
    public int evaluate(int x, int y, int z) {
        return ~exp.evaluate(x, y, z);
    }
}
