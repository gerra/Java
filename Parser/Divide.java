public class Divide<T extends Number> extends BinaryOperation<T> {
    public Divide(Expression3<T> f, Expression3<T> s) {
        super(f, s);
    }
    
    public T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException {
        T f = first.evaluate(x, y, z, calc);
        T s = second.evaluate(x, y, z, calc);
        return calc.div(f, s);
    }
}
