public class Multiply extends BinaryOperation {
    public Multiply(GValue f, GValue s) {
        super(f, s);
    }
    
    public double getValue() {
        return first.getValue() * second.getValue();
    }
}
