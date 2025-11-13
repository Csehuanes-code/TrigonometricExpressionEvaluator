import domine.*;
import domine.ast.*;
import java.util.List;

/**
 * Demonstrates AST visualization capabilities
 */
public class ASTVisualizerDemo {
    public static void main(String[] args) {
        System.out.println("=== Demostración de Visualización del AST ===\n");

        String[] expressions = {
                "3+5*2",
                "sin(x)+cos(y)",
                "2^3^2",
                "cos(x)^2+sin(x)^2",
                "tan(x^2+sin(x))"
        };

        for (String expr : expressions) {
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Expresión: " + expr);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            try {
                Lexer lexer = new Lexer(expr);
                List<Token> tokens = lexer.tokenize();

                System.out.println("\n1. Tokens:");
                for (Token token : tokens) {
                    System.out.printf("   %s : %s\n", token.getTokenType(), token.getLexeme());
                }

                Parser parser = new Parser(tokens);
                ASTNode ast = parser.parseToAST();

                System.out.println("\n2. Árbol de Sintaxis Abstracta:");
                System.out.println(ast.toString());

                System.out.println("\n3. Análisis del Árbol:");
                analyzeAST(ast, "   ");

                System.out.println();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private static void analyzeAST(ASTNode node, String indent) {
        if (node instanceof NumberNode) {
            NumberNode num = (NumberNode) node;
            System.out.println(indent + "- Constante numérica: " + num.getValue());
        } else if (node instanceof VariableNode) {
            VariableNode var = (VariableNode) node;
            System.out.println(indent + "- Variable: " + var.getName());
        } else if (node instanceof BinaryOperationNode) {
            BinaryOperationNode binOp = (BinaryOperationNode) node;
            System.out.println(indent + "- Operación binaria: " + binOp.getOperator());
            System.out.println(indent + "  Operando izquierdo:");
            analyzeAST(binOp.getLeft(), indent + "    ");
            System.out.println(indent + "  Operando derecho:");
            analyzeAST(binOp.getRight(), indent + "    ");
        } else if (node instanceof FunctionNode) {
            FunctionNode func = (FunctionNode) node;
            System.out.println(indent + "- Función: " + func.getFunctionName());
            System.out.println(indent + "  Argumento:");
            analyzeAST(func.getArgument(), indent + "    ");
        }
    }
}
