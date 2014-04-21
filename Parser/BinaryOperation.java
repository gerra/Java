public abstract class BinaryOperation<T extends Number> implements Expression3<T> {
    protected Expression3<T> first, second;
    
    protected BinaryOperation(Expression3<T> first, Expression3<T> second) {
        assert first != null;
        assert second != null;
        this.first = first;
        this.second = second;
    }
}
