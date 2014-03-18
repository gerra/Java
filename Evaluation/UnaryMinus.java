public class UnaryMinus extends UnaryOperation {
    UnaryMinus(Expression3 exp) {
        super(exp);
    }
    
    public double evaluate(double x, double y, double z) {
        return -exp.evaluate(x, y, z);
    }
}
