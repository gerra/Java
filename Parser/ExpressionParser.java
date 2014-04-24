// expr <- factor <- powerFactor <- brackets <- expr ...
//                     \    /  
//                       <-
public class ExpressionParser<T extends Number> {
    private static String s;
    private static int last; 
    private static int balance;
    public Arithmetic<T> calc;
    
    public ExpressionParser(Arithmetic<T> calc) {
        this.calc = calc;
    }
    
    private void deleteSpaces() {
        while (last < s.length() && Character.isWhitespace(s.charAt(last))) {
            last++;
        }
    }
    
    private T getNumber(int sign) throws ParserException {
        char c = s.charAt(last);
        int st = last;
        while (c >= '0' && c <= '9' || c == '.' || c == 'E' || c == '-') {
            last++;
            if (last == s.length()) {
                break;
            }
            c = s.charAt(last);
        }
        String number = s.substring(st, last);
        if (sign == -1) {
            number = "-" + number;
        }
        
        return calc.getNumber(number);
    }
    
    private Expression3<T> operand() throws ParserException {
        deleteSpaces();
        if (last == s.length()) {
            throw new ParserException("Parsing error of " + '"' + s + '"' + " on position " + last + ": expected operand, found null");
        }
        if (s.charAt(last) == '-') {
            last++;
            deleteSpaces();
            if (last == s.length()) {
                throw new ParserException("empty unary minus");
            }
            deleteSpaces();
            char c = s.charAt(last);
            if (c >= '0' && c <= '9') {
                return new Const<T>(getNumber(-1));
            }
            return new UnaryMinus<T>(brackets());
        }
        
        if (s.charAt(last) == '~') {
            last++;
            return new Not<T>(brackets());
        }
        
        if (last + 2 < s.length() && s.substring(last, last + 3).equals("abs")) {
            last += 3;
            return new Abs<T>(brackets());
        }
        
        if (last + 2 < s.length() && s.substring(last, last + 3).equals("sin")) {
            last += 3;
            T a;
            if (a instanceof Double)
                return new Sin(brackets());
            else
                throw new ParserException("Not double");
        }
        
        if (last + 1 < s.length() && s.substring(last, last + 2).equals("lb")) {
            last += 2;
            return new Log2<T>(brackets());
        }
        
        Expression3<T> res;
        char c = s.charAt(last);
        if (c >= '0' && c <= '9') {
            res = new Const<T>(getNumber(1));
        } else if (c >= 'x' && c <= 'z') {
            String name = "";
            name += c;
            last++;
            res = new Variable<T>(name);
        } else {
            throw new ParserException("Parsing error of " + '"' + s + '"' + " on position " + last + ": unknown symbol '" + c + "' of operand");
        }
        return res;
    }
    
    private Expression3<T> brackets() throws ParserException {
        Expression3<T> res;
        deleteSpaces();
        if (last < s.length() && s.charAt(last) == '(') {
            last++;
            balance++;
            res = expr();
            deleteSpaces();
            if (last == s.length() || s.charAt(last) != ')') {
                throw new ParserException("Parsing error of " + '"' + s + '"' + " on position " + last + ": expected ')'");
            }
            last++;
            balance--;
        } else {
            res = operand();
        }
        return res;
    }
    
    private Expression3<T> powerFactor() throws ParserException {
        Expression3<T> res = brackets();
        deleteSpaces();
        if (last < s.length()) {
            if (s.charAt(last) == '^') {
                last++;
                res = new Power<T>(res, powerFactor());
                return res;
            } else {
                char c = s.charAt(last);
                if (c == '+' || c == '-' || c == '*' || c == '/' || (c == ')' && balance > 0)) {
                    return res;
                }
                String error = "Parsing error of " + '"' + s + '"' + " on position " + last;
                if (c == ')' && balance <= 0) {
                    error += ": '(' not found";
                } else {
                    error += ": unknown operation " + c;
                }
                throw new ParserException(error);
            }
        } else {
            return res;
        }
    }
    
    private Expression3<T> factor() throws ParserException {
        Expression3<T> res = powerFactor();
        deleteSpaces();
        while (last < s.length()) {
            switch (s.charAt(last)) {
                case '*':
                    last++;
                    res = new Multiply<T>(res, powerFactor());
                    break;
                case '/':
                    last++;
                    res = new Divide<T>(res, powerFactor());
                    break;
                default:
                    return res;
            }
            deleteSpaces();
        }
        return res;
    }
    
    private Expression3<T> expr() throws ParserException {
        Expression3<T> res = factor();
        deleteSpaces();
        while (last < s.length()) {
            switch (s.charAt(last)) {
                case '+':
                    last++;
                    res = new Add<T>(res, factor());
                    break;
                case '-':
                    last++;
                    res = new Subtract<T>(res, factor());
                    break;
                default:
                    return res;
            }
            deleteSpaces();
        }
        return res;
    }
    
    public Expression3<T> parse(String input) throws ParserException {
        balance = 0;
        s = input;
        last = 0;
        return expr();
    }
    
    public T evaluate(String s, String x, String y, String z) throws MyCalcException, ParserException {
        Expression3<T> exp = parse(s);
        return exp.evaluate(calc.getNumber(x), calc.getNumber(y), calc.getNumber(z), calc);
    }
    
    public void check(String s) {
        Expression3<T> exp = null;
        try {
            exp = parse(s);
        } catch(ParserException e) {
            System.err.println(e.getMessage());
        }
        for (int x = -100; x <= 100; x++) {
            for (int y = -100; y <= 100; y++) {
                try {
                    System.out.println(exp.evaluate(calc.getNumber("" + x), calc.getNumber("" + y), calc.getNumber("3"), calc));
                } catch(ParserException e) {
                    System.out.println(e.getMessage());
                } catch (MyCalcException e) {
                    System.out.println("error");
                }
            }
        }
    }
}
