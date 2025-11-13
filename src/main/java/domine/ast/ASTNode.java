package domine.ast;

/**
 * Clase abstracta base para todos los nodos del AST
 */
public abstract class ASTNode {
    /**
     * Método abstracto para evaluar el nodo
     * @return El valor numérico del nodo
     */
    public abstract double evaluate() throws Exception;

    /**
     * Método abstracto para representar el árbol en formato texto
     * @param indent Nivel de indentación
     * @return Representación en texto del árbol
     */
    public abstract String toTreeString(String indent);

    @Override
    public String toString() {
        return toTreeString("");
    }
}
