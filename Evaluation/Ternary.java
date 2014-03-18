public class Ternary extends TernaryOperation {
    public Ternary(Expression3 f, Expression3 s, Expression3 t) {
        super(f, s, t);
    }
    
    public double evaluate(double x, double y, double z) {
        return first.evaluate(x, y, z) != 0 ? second.evaluate(x, y, z) : third.evaluate(x, y, z);
    }
}
