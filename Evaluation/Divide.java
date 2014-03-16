public class Divide extends BinaryOperation {
    public Divide(GValue f, GValue s) {
        super(f, s);
    }
    
    public double getValue() {
        assert second.getValue() != 0;
        return first.getValue() / second.getValue();
    }
}
