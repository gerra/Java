public class DoubleArithmetic implements Arithmetic<Double> {
    public Double ter(Double x, Double y, Double z) {
        return x.doubleValue() != 0 ? y : z;
    }
    
    public Double add(Double x, Double y) throws MyCalcException {
        double res = x.doubleValue() + y.doubleValue();
        return Double.valueOf(res);
    }
    
    public Double sub(Double x, Double y) throws MyCalcException {
        double res = x.doubleValue() - y.doubleValue();
        return Double.valueOf(res);
    }
    
    public Double mul(Double x, Double y) throws MyCalcException {
        double res = x.doubleValue() * y.doubleValue();
        return Double.valueOf(res);
    }
    
    public Double div(Double x, Double y) throws MyCalcException {
        //if (y.doubleValue() == 0) {
        //    throw new DBZException("division by zero");
        //    return new Double("NaN");
        //  }
        double res = x.doubleValue() / y.doubleValue();
        //if (res != res) {
        //    throw new MyCalcException("NaN");
        //}
        return Double.valueOf(res);
    }
    
    public Double pow(Double x, Double y) throws MyCalcException {
        double f = x.doubleValue();
        double s = y.doubleValue();
        
        if (s < 0) {
            throw new NegativePower("negative power");
        }
    
        double res = Math.pow(f, s);
        return Double.valueOf(res);
    }
    
    public Double abs(Double x) throws MyCalcException {
        double res = Math.abs(x.doubleValue());
        return Double.valueOf(res);
    }
    
    public Double neg(Double x) throws MyCalcException {
        double res = x.doubleValue() * (-1.0);
        return Double.valueOf(res);
    }
    
    public Double lb(Double x) throws MyCalcException {
        double res = x.doubleValue();
        if (res < -1e-7) {
            throw new LogNegException("log of negative expression");
        }
        if (Math.abs(res) <= 1e-7) {
            throw new LogNegException("log of nil expression");
        }
        res = Math.log(res) / Math.log(2.0);
        if (res != res) {
            throw new MyCalcException("NaN");
        }
        return Double.valueOf(res);
    }
    
    public Double not(Double x) throws MyCalcException {
        throw new MyCalcException("~ Double");
    }
    
    public Double getNumber(String s) throws ParserException {
        try {
            return new Double(s);
        } catch (NumberFormatException e) {
            throw new ParserException("wrong number");
        }
    }
    
    //public Double sin(Double x) {
    //    return Double.valueOf(Math.sin(x.doubleValue()));
    //}
}
