public class Multiply extends BinaryOperation {
    public Multiply(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public double evaluate(double x, double y, double z) {
        return first.evaluate(x, y, z) * second.evaluate(x, y, z);
    }
}
