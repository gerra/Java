public class Const implements GValue {
    private final double value;
    
    Const(double v) {
        value = v;
    }
    
    public double getValue() {
        return value;
    }
    
    public void initialize(double v) {
    }
}
