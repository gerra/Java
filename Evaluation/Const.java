public class Const implements Expression3 {
    private final double value;
    
    Const(double v) {
        value = v;
    }

    public double evaluate(double x, double y, double z) {
        return value;
    }
}
