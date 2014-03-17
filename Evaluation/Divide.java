public class Divide extends BinaryOperation {
    public Divide(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public double evaluate(double x, double y, double z) {
        assert second.evaluate(x, y, z) != 0;
        return first.evaluate(x, y, z) / second.evaluate(x, y, z);
    }
}
