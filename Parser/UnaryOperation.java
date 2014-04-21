public abstract class UnaryOperation<T extends Number> implements Expression3<T> {
    protected Expression3<T> exp;
    
    protected UnaryOperation(Expression3<T> exp) {
        assert exp != null;
        this.exp = exp;
    }
}
