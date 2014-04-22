public interface Arithmetic<T extends Number> {
    T ter(T x, T y, T z);
    T add(T x, T y) throws MyCalcException;
    T sub(T x, T y) throws MyCalcException;
    T mul(T x, T y) throws MyCalcException;
    T div(T x, T y) throws MyCalcException;
    T pow(T x, T y) throws MyCalcException;
    T abs(T x) throws MyCalcException;
    T neg(T x) throws MyCalcException;
    T lb(T x) throws MyCalcException;
    T not(T x) throws MyCalcException;
    T getNumber(String s) throws ParserException;
}
