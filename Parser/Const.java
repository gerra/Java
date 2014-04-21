public class Const<T extends Number> implements Expression3<T> {
    private final T value;
    
    public Const(T value) {
        this.value = value;
    }

    public T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException {
        return value;
    }
}
