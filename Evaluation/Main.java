public class Main {
    public static void main(String args[]) {
        double x = Double.parseDouble(args[0]);
        double res;
        res = new Add (
                  new Subtract (
                      new Multiply(
                          new Variable("x"),
                          new Variable("y") 
                      ),
                      new Multiply(
                          new Const(2),
                          new Variable("z")
                      )
                  ), 
                  new Const(1)
              ).evaluate(x, x, x);
              
        double res0 = new UnaryMinus(new Multiply(
                          new Variable("x"),
                          new Variable("y") 
                      )).evaluate(x, x, x);
        res = new Ternary(
                new Variable("x"), 
                new Variable("y"), 
                new Variable("z")
                ).evaluate(1, 2, 3);
        System.out.println(res0 + " " + res);
        
    }
}
