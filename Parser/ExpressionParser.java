// expr <- factor <- powerFactor <- brackets <- expr ...
//                     \    /  
//                       <-
public class ExpressionParser {
    static String s;
    static int last; 
    static int balance;
    
    static void deleteSpaces() {
        while (last < s.length() && Character.isWhitespace(s.charAt(last))) {
            last++;
        }
    }
    
    static int getNumber(int sign) throws ParserException {
        char c = s.charAt(last);
        double val = 0;
        while (c >= '0' && c <= '9' && last < s.length()) {
            val = val * 10 + sign * (c - '0');
            if (val < Integer.MIN_VALUE || val > Integer.MAX_VALUE) {
                throw new ParserException("too big number");
            }
            last++;
            if (last == s.length()) {
                break;
            }
            c = s.charAt(last);
        }
        return (int)val;
    }
    
    static Expression3 operand() throws ParserException {
        deleteSpaces();
        if (last == s.length()) {
            throw new ParserException("Parsing error of " + '"' + s + '"' + " on position " + last + ": expected operand, found null");
        }
        
        if (s.charAt(last) == '-') {
            last++;
            deleteSpaces();
            char c = s.charAt(last);
            if (c >= '0' && c <= '9') {
                return new Const(getNumber(-1));
            }
            return new UnaryMinus(brackets());
        }
        
        if (s.charAt(last) == '~') {
            last++;
            return new Not(brackets());
        }
        
        if (last + 2 < s.length() && s.substring(last, last + 3).equals("abs")) {
            last += 3;
            return new Abs(brackets());
        }
        
        if (last + 1 < s.length() && s.substring(last, last + 2).equals("lb")) {
            last += 2;
            return new Log2(brackets());
        }
        
        Expression3 res;
        char c = s.charAt(last);
        if (c >= '0' && c <= '9') {
            res = new Const(getNumber(1));
        } else if (c >= 'x' && c <= 'z') {
            String name = "";
            name += c;
            last++;
            res = new Variable(name);
        } else {
            throw new ParserException("Parsing error of " + '"' + s + '"' + " on position " + last + ": unknown symbol '" + c + "' of operand");
        }
        return res;
    }
    
    static Expression3 brackets() throws ParserException {
        Expression3 res;
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
    
    static Expression3 powerFactor() throws ParserException {
        Expression3 res = brackets();
        deleteSpaces();
        if (last < s.length()) {
            if (s.charAt(last) == '^') {
                last++;
                res = new Power(res, powerFactor());
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
    
    static Expression3 factor() throws ParserException {
        Expression3 res = powerFactor();
        deleteSpaces();
        while (last < s.length()) {
            switch (s.charAt(last)) {
                case '*':
                    last++;
                    res = new Multiply(res, powerFactor());
                    break;
                case '/':
                    last++;
                    res = new Divide(res, powerFactor());
                    break;
                default:
                    return res;
            }
            deleteSpaces();
        }
        return res;
    }
    
    static Expression3 expr() throws ParserException {
        Expression3 res = factor();
        deleteSpaces();
        while (last < s.length()) {
            switch (s.charAt(last)) {
                case '+':
                    last++;
                    res = new Add(res, factor());
                    break;
                case '-':
                    last++;
                    res = new Subtract(res, factor());
                    break;
                default:
                    return res;
            }
            deleteSpaces();
        }
        return res;
    }
    
    static Expression3 parse(String input) throws ParserException {
        balance = 0;
        s = input;
        last = 0;
        return expr();
    }
    
    public static void main(String args[]) {
       // BigInteger a;
        
        int res = 0;
        try {
            Expression3 ex = parse(args[0]);
            if (ex != null) {
                res = ex.evaluate(1, 2, 0);
                System.out.println(res);
            }
        } catch (ParserException e) {
            System.err.println(e.getMessage());
        } catch (MyCalcException e) {
            System.err.println(e.getMessage());
        }
    }
}
