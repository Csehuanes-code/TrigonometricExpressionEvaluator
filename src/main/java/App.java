import domine.Lexer;
import domine.Parser;
import domine.Token;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class App 
{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Parser y Evaluador de Expresiones Trigonométricas ===");
        System.out.println("Expresiones válidas:");
        System.out.println("  - sin(3.14/2) + cos(0)*2^2");
        System.out.println("  - sin(x) + cos(y)");
        System.out.println("  - cos(x)^2 + sin(x)^2");
        System.out.println("  - tan(x^2 + sin(x))");
        System.out.println();

        while (true) {
            System.out.print("Ingrese una expresión (o 'salir' para terminar): ");
            String input = scanner.nextLine();


            if (input.equalsIgnoreCase("salir") || input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                // Fase 1: Análisis léxico (tokenización)
                Lexer lexer = new Lexer(input);
                List<Token> tokens = lexer.tokenize();

                System.out.println("\n--- Tokens generados ---");
                for (Token token : tokens) {
                    System.out.println("  " + token.getTokenType() + " : " + token.getLexeme());
                }

                // Fase 2: Análisis sintáctico y evaluación
                Parser parser = new Parser(tokens);
                double result = parser.parse();

                System.out.println("\n--- Resultado ---");
                System.out.printf("  %.6f\n", result);

            } catch (Exception e) {
                System.out.println("\n[ERROR] " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
        System.out.println("¡Hasta luego!");
    }
}
