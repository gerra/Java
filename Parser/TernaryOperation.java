public abstract class TernaryOperation<T extends Number> implements Expression3<T> {
    protected Expression3<T> first, second, third;
    
    protected TernaryOperation(Expression3<T> first, Expression3<T> second, Expression3<T> third) {
        assert first != null;
        assert second != null;
        assert third != null;
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
