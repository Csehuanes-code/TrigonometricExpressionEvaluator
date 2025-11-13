package domine;

import domine.ast.ASTNode;
import java.util.Map;

/**
 * Evaluador que recorre y evalúa el AST
 */
public class Evaluator {

    /**
     * Evalúa un AST y retorna el resultado
     * @param root El nodo raíz del AST
     * @return El resultado de la evaluación
     */
    public static double evaluate(ASTNode root) throws Exception {
        if (root == null) {
            throw new Exception("AST vacío");
        }
        return root.evaluate();
    }

    /**
     * Evalúa un AST con valores de variables proporcionados
     * @param root El nodo raíz del AST
     * @param variables Mapa de variables y sus valores
     * @return El resultado de la evaluación
     */
    public static double evaluate(ASTNode root, Map<String, Double> variables) throws Exception {
        if (root == null) {
            throw new Exception("AST vacío");
        }
        // Las variables ya están inyectadas en el Parser
        return root.evaluate();
    }
}
