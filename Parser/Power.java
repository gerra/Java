public class Power<T extends Number> extends BinaryOperation<T> {
    Power(Expression3<T> first, Expression3<T> second) {
        super(first, second);
    }
    
    public T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException {
        T f = first.evaluate(x, y, z, calc);
        T s = second.evaluate(x, y, z, calc);
        return calc.pow(f, s);
    }
}
