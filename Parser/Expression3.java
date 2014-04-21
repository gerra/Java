public interface Expression3 <T extends Number> {
    T evaluate(T x, T y, T z, Arithmetic<T> calc) throws MyCalcException;
}
