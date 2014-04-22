import java.math.BigInteger;
public class BigIntegerArithmetic implements Arithmetic<BigInteger> {
    public BigInteger ter(BigInteger x, BigInteger y, BigInteger z) {
        return x.equals(BigInteger.ZERO) ? y : z;
    }
    
    public BigInteger add(BigInteger x, BigInteger y) throws MyCalcException {
        return x.add(y);
    }
    
    public BigInteger sub(BigInteger x, BigInteger y) throws MyCalcException {
        return x.subtract(y);
    }
    
    public BigInteger mul(BigInteger x, BigInteger y) throws MyCalcException {
        return x.multiply(y);
    }
    
    public BigInteger div(BigInteger x, BigInteger y) throws MyCalcException {
        if (y.equals(BigInteger.ZERO)) {
            throw new DBZException("division by zero");
        }
        return x.divide(y);
    }
    
    public BigInteger pow(BigInteger x, BigInteger y) throws MyCalcException {
        if (y.signum() < 0) {
            throw new NegativePower("negative power(bi)");
        }
       /* if (y.compareTo(BigInteger.valueOf((long)Integer.MAX_VALUE)) == 1 || y.compareTo(BigInteger.valueOf((long)Integer.MIN_VALUE)) == -1) {
            throw new OverflowException("pow overflow(bi)");
        }*/
        return x.pow(y.intValue());
    }
    
    public BigInteger abs(BigInteger x) throws MyCalcException {
        return x.abs();
    }
    
    public BigInteger neg(BigInteger x) throws MyCalcException {
        return x.negate();
    }
    
    public BigInteger lb(BigInteger x) throws MyCalcException {
        if (x.signum() < 0) {
            throw new LogNegException("log of negative expression");
        }
        if (x.signum() == 0) {
            throw new LogNegException("log of nil expression");
        }
        return BigInteger.valueOf(x.bitLength() - 1);
    }
    
    public BigInteger not(BigInteger x) throws MyCalcException {
        return x.not();
    }
    
    public BigInteger getNumber(String s) throws ParserException {
        try {
            return new BigInteger(s);
        } catch (NumberFormatException e) {
            throw new ParserException("wrong number");
        }
    }
}
