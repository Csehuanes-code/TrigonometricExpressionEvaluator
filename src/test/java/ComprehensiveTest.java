import domine.Lexer;
import domine.Parser;
import domine.Token;

import java.util.List;
import java.util.Scanner;

/**
 * Clase de pruebas comprehensiva que implementa todos los casos de prueba
 * especificados en el documento del proyecto.
 *
 * Esta clase ejecuta un mínimo de 20 casos de prueba divididos en:
 * - Casos correctos que deben evaluarse correctamente
 * - Casos erróneos que deben producir mensajes de error claros
 *
 * Cada caso incluye su respectiva validación matemática.
 */
public class ComprehensiveTest {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SUITE DE PRUEBAS COMPREHENSIVA - EVALUADOR DE EXPRESIONES       ║");
        System.out.println("║  Expresiones Aritméticas y Trigonométricas con AST               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝\n");

        // ========== CASOS CORRECTOS (Deben evaluarse correctamente) ==========
        System.out.println("═══════════════════════════════════════════════════════════════════");
        System.out.println("                    CASOS CORRECTOS                                ");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");

        // Test 1: Precedencia de operadores (multiplicación antes que suma)
        testExpression("3 + 4 * 2", 11.0, "Precedencia: multiplicación antes que suma");

        // Test 2: Paréntesis cambiando precedencia
        testExpression("(3 + 4) * 2", 14.0, "Paréntesis modificando precedencia");

        // Test 3: Negación unaria con potencia - el negativo se aplica después
        testExpression("-2^2", -4.0, "Negación unaria: -(2^2) = -4");

        // Test 4: Asociatividad derecha en potencia
        testExpression("2^3^2", 512.0, "Asociatividad derecha: 2^(3^2) = 2^9 = 512");

        // Test 5: Función seno con pi/2
        testExpression("sin(3.14159265359/2)", 1.0, "sin(π/2) ≈ 1", 0.0001);

        // Test 6: Coseno de 0 más producto
        testExpression("cos(0) + sin(3.14159265359)", 1.0, "cos(0) + sin(π) = 1 + 0 ≈ 1", 0.0001);

        // Test 7: Números decimales con punto inicial
        testExpression("3.5 * 2.0 + .5", 7.5, "Operaciones con decimales incluyendo .5");

        // Test 8: Función tangente
        testExpression("tan(0.785398163)", 1.0, "tan(π/4) ≈ 1", 0.0001);

        // Test 9: Raíz cuadrada usando potencia
        testExpression("2^(1/2)", Math.sqrt(2), "2^(1/2) = √2 ≈ 1.414", 0.0001);

        // Test 10: Expresión compleja anidada
        testExpression("sin(cos(0)) + 2 * 3", Math.sin(1) + 6, "sin(cos(0)) + 6 ≈ 6.841", 0.001);

        // Test 11: División simple
        testExpression("10 / 2", 5.0, "División simple");

        // Test 12: Múltiples operaciones
        testExpression("2 + 3 * 4 - 5", 9.0, "2 + 12 - 5 = 9");

        // Test 13: Potencia con decimales
        testExpression("4^0.5", 2.0, "4^0.5 = √4 = 2");

        // Test 14: Coseno al cuadrado más seno al cuadrado (identidad trigonométrica)
        testExpression("cos(1)^2 + sin(1)^2", 1.0, "Identidad: cos²(x) + sin²(x) = 1", 0.0001);

        // Test 15: Notación científica
        testExpression("2E2 + 50", 250.0, "Notación científica: 2E2 = 200");

        // Test 16: Números negativos en expresión
        testExpression("-5 + 3", -2.0, "Suma con número negativo");

        // Test 17: Multiplicación de funciones trigonométricas
        testExpression("sin(0) * cos(0)", 0.0, "sin(0) * cos(0) = 0 * 1 = 0", 0.0001);

        // Test 18: División con paréntesis
        testExpression("(8 + 2) / (3 - 1)", 5.0, "(8+2)/(3-1) = 10/2 = 5");

        // Test 19: Expresión compleja con todas las operaciones
        testExpression("2^3 + 4*5 - 6/2", 25.0, "8 + 20 - 3 = 25");

        // Test 20: Funciones trigonométricas anidadas
        testExpression("tan(sin(0))", 0.0, "tan(sin(0)) = tan(0) = 0", 0.0001);

        // ========== CASOS ERRÓNEOS (Deben producir errores claros) ==========
        System.out.println("\n═══════════════════════════════════════════════════════════════════");
        System.out.println("                    CASOS ERRÓNEOS                                 ");
        System.out.println("═══════════════════════════════════════════════════════════════════\n");

        // Test 21: Error sintáctico - operador sin operando derecho
        testErrorExpression("3 + * 4", "Operador sin operando derecho");

        // Test 22: Error sintáctico - función sin argumentos
        testErrorExpression("sin()", "Función sin argumentos");

        // Test 23: Error de ejecución - división por cero
        testErrorExpression("1 / 0", "División por cero");

        // Test 24: Error sintáctico - potencia incompleta
        testErrorExpression("2 ^", "Operador de potencia incompleto");

        // Test 25: Error sintáctico - número mal formado
        testErrorExpression("5..3", "Número con doble punto decimal");

        // Test 26: Error de paréntesis sin cerrar
        testErrorExpression("(3 + 4", "Paréntesis sin cerrar");

        // Test 27: Error de paréntesis sin abrir
        testErrorExpression("3 + 4)", "Paréntesis sin abrir");

        // Test 28: Error - operador al inicio
        testErrorExpression("* 3 + 4", "Expresión inicia con operador");

        // Test 29: Error - paréntesis vacíos
        testErrorExpression("2 + ()", "Paréntesis vacíos");

        // Test 30: Error - función desconocida
        testErrorExpression("sqrt(4)", "Función no reconocida");

        // ========== RESUMEN DE RESULTADOS ==========
        printSummary();
    }

    /**
     * Prueba una expresión y verifica que el resultado coincida con el esperado
     * @param expression Expresión a evaluar
     * @param expected Resultado esperado
     * @param description Descripción del caso de prueba
     */
    private static void testExpression(String expression, double expected, String description) {
        testExpression(expression, expected, description, 0.00001);
    }

    /**
     * Prueba una expresión con tolerancia personalizada para errores de punto flotante
     * @param expression Expresión a evaluar
     * @param expected Resultado esperado
     * @param description Descripción del caso de prueba
     * @param tolerance Tolerancia para la comparación
     */
    private static void testExpression(String expression, double expected, String description, double tolerance) {
        totalTests++;
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ Test #%-2d: %-55s │%n", totalTests, description);
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ Expresión: %-52s │%n", expression);
        System.out.printf("│ Esperado:  %-52.6f │%n", expected);

        try {
            Lexer lexer = new Lexer(expression);
            List<Token> tokens = lexer.tokenize();
            Parser parser = new Parser(tokens);
            double result = parser.parse();

            System.out.printf("│ Resultado: %-52.6f │%n", result);

            // Comparación con tolerancia para errores de punto flotante
            if (Math.abs(result - expected) <= tolerance) {
                System.out.println("│ Estado: ✓ PASÓ                                                  │");
                passedTests++;
            } else {
                System.out.println("│ Estado: ✗ FALLÓ                                                 │");
                System.out.printf("│ Diferencia: %-48.10f │%n", Math.abs(result - expected));
                failedTests++;
            }

        } catch (Exception e) {
            System.out.printf("│ Error: %-56s │%n", e.getMessage());
            System.out.println("│ Estado: ✗ FALLÓ (Excepción inesperada)                         │");
            failedTests++;
        }

        System.out.println("└─────────────────────────────────────────────────────────────────┘\n");
    }

    /**
     * Prueba una expresión que debe generar un error
     * @param expression Expresión errónea
     * @param description Descripción del error esperado
     */
    private static void testErrorExpression(String expression, String description) {
        totalTests++;
        System.out.println("┌─────────────────────────────────────────────────────────────────┐");
        System.out.printf("│ Test #%-2d: %-55s │%n", totalTests, description);
        System.out.println("├─────────────────────────────────────────────────────────────────┤");
        System.out.printf("│ Expresión: %-52s │%n", expression);
        System.out.println("│ Se espera: ERROR                                                │");

        try {
            Lexer lexer = new Lexer(expression);
            List<Token> tokens = lexer.tokenize();
            Parser parser = new Parser(tokens);
            double result = parser.parse();

            System.out.printf("│ Resultado: %-52.6f │%n", result);
            System.out.println("│ Estado: ✗ FALLÓ (Se esperaba error pero se evaluó)             │");
            failedTests++;

        } catch (Exception e) {
            System.out.printf("│ Error capturado: %-47s │%n",
                    e.getMessage().substring(0, Math.min(e.getMessage().length(), 47)));
            System.out.println("│ Estado: ✓ PASÓ (Error detectado correctamente)                 │");
            passedTests++;
        }

        System.out.println("└─────────────────────────────────────────────────────────────────┘\n");
    }

    /**
     * Imprime el resumen final de todas las pruebas ejecutadas
     */
    private static void printSummary() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      RESUMEN DE PRUEBAS                           ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Total de pruebas:    %-40d ║%n", totalTests);
        System.out.printf("║ Pruebas exitosas:    %-40d ║%n", passedTests);
        System.out.printf("║ Pruebas fallidas:    %-40d ║%n", failedTests);
        System.out.printf("║ Porcentaje de éxito: %-38.2f%% ║%n", (passedTests * 100.0 / totalTests));
        System.out.println("╚═══════════════════════════════════════════════════════════════════╝");

        if (failedTests == 0) {
            System.out.println("\n🎉 ¡Todas las pruebas pasaron exitosamente!");
        } else {
            System.out.println("\n⚠️  Algunas pruebas fallaron. Revisar los detalles arriba.");
        }
    }
}
