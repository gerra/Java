public abstract class BinaryOperation implements GValue {
    GValue first, second;
    
    public BinaryOperation(GValue f, GValue s) {
        assert f != null;
        assert s != null;
        first = f;
        second = s;
    }
        
    public final double evaluate(double varValue) {
        initialize(varValue);
        return getValue();
    }
    
    public final void initialize(double varValue) {
        first.initialize(varValue);
        second.initialize(varValue);
    }
}
