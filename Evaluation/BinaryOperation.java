public abstract class BinaryOperation implements Expression3 {
    Expression3 first, second;
    
    public BinaryOperation(Expression3 f, Expression3 s) {
        assert f != null;
        assert s != null;
        first = f;
        second = s;
    }
}
