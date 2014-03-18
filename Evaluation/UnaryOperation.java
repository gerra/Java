public abstract class UnaryOperation implements Expression3 {
    Expression3 exp;
    
    protected UnaryOperation(Expression3 exp) {
        assert exp != null;
        this.exp = exp;
    }
}
