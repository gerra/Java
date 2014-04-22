public class IntegerArithmetic implements Arithmetic<Integer> {
    public Integer ter(Integer x, Integer y, Integer z) {
        return x.intValue() != 0 ? y : z;
    }
    
    public Integer add(Integer x, Integer y) throws MyCalcException {
        long res = x.longValue() + y.longValue();
       /* if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("add overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer sub(Integer x, Integer y) throws MyCalcException {
        long res = x.longValue() - y.longValue();
        /*if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("sub overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer mul(Integer x, Integer y) throws MyCalcException {
        long res = x.longValue() * y.longValue();
       /* if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("mul overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer div(Integer x, Integer y) throws MyCalcException {
        if (y.longValue() == 0) {
            throw new DBZException("division by zero");
        }
        long res = x.longValue() / y.longValue();
        /*if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("div overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer pow(Integer x, Integer y) throws MyCalcException {
        double f = x.doubleValue();
        double s = y.doubleValue();
        
        if (s < 0) {
            throw new NegativePower("negative power");
        }
        double res = Math.pow(f, s);
       /* if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("pow overflow");
        }*/
        
        return Integer.valueOf((int)res);
    }
    
    public Integer abs(Integer x) throws MyCalcException {
        long res = Math.abs(x.longValue());
        /*if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("abs overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer neg(Integer x) throws MyCalcException {
        long res = x.longValue() * (-1);
       /* if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            throw new OverflowException("neg overflow");
        }*/
        return Integer.valueOf((int)res);
    }
    
    public Integer lb(Integer x) throws MyCalcException {
        int res = x.intValue();
        if (res < 0) {
            throw new LogNegException("log of negative expression");
        }
        if (res == 0) {
            throw new LogNegException("log of nil expression");
        }
        res = (int)(Math.log((double)res) / Math.log(2.0));
        return Integer.valueOf(res);
    }
    
    public Integer not(Integer x) throws MyCalcException {
        int res = x.intValue();
        res = ~res;
        return Integer.valueOf(res);
    }
    
    public Integer getNumber(String s) throws ParserException {
        try {
            return new Integer(s);
        } catch (NumberFormatException e) {
            throw new ParserException("wrong number");
        }
    }
}
