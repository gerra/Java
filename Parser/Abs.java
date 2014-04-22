public class Abs<T extends Number> extends UnaryOperation<T> {
    Abs(Expression3<T> exp) {
        super(exp);
    }
    
    public T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException {
        T e = exp.evaluate(x, y, z, calc);
        return calc.abs(e);
    }
}
