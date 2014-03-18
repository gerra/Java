public abstract class TernaryOperation implements Expression3 {
    Expression3 first, second, third;
    
    protected TernaryOperation(Expression3 first, Expression3 second, Expression3 third) {
        assert first != null;
        assert second != null;
        assert third != null;
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
