public class Variable implements Expression3 {
    private String name;
    
    public Variable (String name) {
        assert name != null;
        this.name = name;
    }
    
    public double evaluate(double x, double y, double z) {
        if (name.equals("x")) {
            return x;
        } else if (name.equals("y")) {
            return y;
        } else if (name.equals("z")) {
            return z;
        } else {
            return 0.0;
        }
    }
}
