public class Ternary<T extends Number> extends TernaryOperation<T> {
    public Ternary(Expression3<T> f, Expression3<T> s, Expression3<T> t) {
        super(f, s, t);
    }
    
    public T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException {
        T f = first.evaluate(x, y, z, calc);
        T s = second.evaluate(x, y, z, calc);
        T t = third.evaluate(x, y, z, calc);
        return calc.ter(f, s, t);
    }
}
