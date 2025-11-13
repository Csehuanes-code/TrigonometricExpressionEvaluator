import domine.Evaluator;
import domine.Lexer;
import domine.Parser;
import domine.Token;
import domine.ast.ASTNode;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    public static void main(String[] args) {
        System.out.println("=== Casos de Prueba con AST ===\n");

        String[] testCases = {
                "sin(3.14/2)+cos(0)*2^2",
                "3+5*2",
                "2^3^2",
                "(3+5)*2",
                "cos(0)",
                "sin(0)+cos(0)",
                "2*3+4*5",
                "10/2-3",
                "2^10",
                "sin(3.14159265359/2)"
        };

        for (String testCase : testCases) {
            System.out.println("Expresión: " + testCase);
            try {
                Lexer lexer = new Lexer(testCase);
                List<Token> tokens = lexer.tokenize();

                Parser parser = new Parser(tokens);
                ASTNode ast = parser.parseToAST();

                System.out.println("AST:");
                System.out.println(ast.toString());

                double result = Evaluator.evaluate(ast);

                System.out.printf("Resultado: %.6f\n", result);
                System.out.println("✓ Aceptada\n");

            } catch (Exception e) {
                System.out.println("✗ Error: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Casos de Prueba con Variables ===\n");

        // Test cases with predefined variable values
        TestCase[] testCases = {
                new TestCase("sin(x)", new String[]{"x"}, new double[]{0}, 0.0),
                new TestCase("sin(x)", new String[]{"x"}, new double[]{3.14159265359/2}, 1.0),
                new TestCase("cos(x)^2+sin(x)^2", new String[]{"x"}, new double[]{0.5}, 1.0),
                new TestCase("x+y", new String[]{"x", "y"}, new double[]{3, 5}, 8.0),
                new TestCase("sin(x)*cos(y)", new String[]{"x", "y"}, new double[]{0, 0}, 0.0),
                new TestCase("tan(x^2+sin(x))", new String[]{"x"}, new double[]{0}, 0.0),
                new TestCase("x^2+y^2", new String[]{"x", "y"}, new double[]{3, 4}, 25.0),
                new TestCase("2*x+3*y", new String[]{"x", "y"}, new double[]{1, 2}, 8.0)
        };

        for (TestCase test : testCases) {
            System.out.println("Expresión: " + test.expression);
            System.out.print("Variables: ");
            for (int i = 0; i < test.varNames.length; i++) {
                System.out.print(test.varNames[i] + "=" + test.varValues[i]);
                if (i < test.varNames.length - 1) System.out.print(", ");
            }
            System.out.println();

            try {
                Lexer lexer = new Lexer(test.expression);
                List<Token> tokens = lexer.tokenize();

                Parser parser = new Parser(tokens);

                // Set variable values
                for (int i = 0; i < test.varNames.length; i++) {
                    parser.setVariableValue(test.varNames[i], test.varValues[i]);
                }

                // Build AST
                ASTNode ast = parser.parseToAST();

                System.out.println("AST:");
                System.out.println(ast.toString());

                // Evaluate
                double result = Evaluator.evaluate(ast);

                System.out.printf("Resultado: %.6f\n", result);
                System.out.printf("Esperado: %.6f\n", test.expectedResult);

                // Check if result matches expected (with tolerance)
                if (Math.abs(result - test.expectedResult) < 0.0001) {
                    System.out.println("✓ Test pasado\n");
                } else {
                    System.out.println("✗ Test fallido\n");
                }

            } catch (Exception e) {
                System.out.println("✗ Error: " + e.getMessage() + "\n");
            }
        }
    }

    static class TestCase {
        String expression;
        String[] varNames;
        double[] varValues;
        double expectedResult;

        TestCase(String expression, String[] varNames, double[] varValues, double expectedResult) {
            this.expression = expression;
            this.varNames = varNames;
            this.varValues = varValues;
            this.expectedResult = expectedResult;
        }
    }
}
