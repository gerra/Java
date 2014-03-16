public class Subtract extends BinaryOperation {
    public Subtract(GValue f, GValue s) {
        super(f, s);
    }
    
    public double getValue() {
        return first.getValue() - second.getValue();
    }
}
