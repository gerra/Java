public class Variable implements Expression3 {
    String name;
    
    Variable (String n) {
        assert n != null;
        name = n;
    }
    
    public double evaluate(double x, double y, double z) {
        if (name == "x") {
            return x;
        } else if (name == "y") {
            return y;
        } else if (name == "z") {
            return z;
        } else {
            return 0.0;
        }
    }
}
