import domine.Lexer;
import domine.Parser;
import domine.Token;
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
        System.out.println("=== Casos de Prueba ===\n");

        String[] testCases = {
                "sin(3.14/2)+cos(0)*2^2",
                "3+5*2",
                "2^3^2",
                "(3+5)*2",
                "cos(0)",
                "sin(0)+cos(0)",
                "2*3+4*5",
                "10/2 - 3",
                "2^10",
                "sin(3.14159265359/2)"
        };

        for (String testCase : testCases) {
            System.out.println("Expresión: " + testCase);
            try {
                Lexer lexer = new Lexer(testCase);
                List<Token> tokens = lexer.tokenize();

                Parser parser = new Parser(tokens);
                double result = parser.parse();

                System.out.printf("Resultado: %.6f\n", result);
                System.out.println("✓ Aceptada\n");

            } catch (Exception e) {
                System.out.println("✗ Error: " + e.getMessage() + "\n");
            }
        }
    }
}
