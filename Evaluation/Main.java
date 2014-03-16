// res = x2 - 2x + 1
public class Main {
    public static void main(String args[]) {
        double x = Double.parseDouble(args[0]);
        double res;
        res = new Add (
                  new Subtract (
                      new Multiply(
                          new Variable("x"),
                          new Variable("x") 
                      ),
                      new Multiply(
                          new Const(2),
                          new Variable("x")
                      )
                  ), 
                  new Const(1)
              ).evaluate(x);
        System.out.println(res);
    }
}
