public class Add extends BinaryOperation {
    public Add(GValue f, GValue s) {
        super(f, s);
    }
    
    public double getValue() {
        return first.getValue() + second.getValue();
    }
}
