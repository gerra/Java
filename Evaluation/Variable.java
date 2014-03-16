public class Variable implements GValue {
    String name;
    boolean initialized = false;
    private double value;
    
    Variable (String n) {
        assert n != null;
        name = n;
    }
    
    public double getValue() {
        assert initialized == true;
        return value;
    }
    
    public void initialize(double v) {
        value = v;
        initialized = true;
    }
}
