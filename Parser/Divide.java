public class Divide extends BinaryOperation {
    public Divide(Expression3 f, Expression3 s) {
        super(f, s);
    }
    
    public int evaluate(int x, int y, int z) throws MyCalcException {
        int divident = second.evaluate(x, y, z);
        if (divident == 0) {
            throw new DBZException("division by zero");
        }
        return first.evaluate(x, y, z) / divident;
    }
}
