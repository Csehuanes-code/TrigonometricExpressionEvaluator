package domine.ast;

import lombok.Getter;

/**
 * Nodo que representa una función trigonométrica (sin, cos, tan)
 */
@Getter
public class FunctionNode extends ASTNode {
    private final String functionName;
    private final ASTNode argument;

    public FunctionNode(String functionName, ASTNode argument) {
        this.functionName = functionName;
        this.argument = argument;
    }

    @Override
    public double evaluate() throws Exception {
        double argValue = argument.evaluate();

        return switch (functionName.toLowerCase()) {
            case "sen", "sin" -> Math.sin(argValue);
            case "cos" -> Math.cos(argValue);
            case "tan" -> Math.tan(argValue);
            default -> throw new Exception("Función desconocida: " + functionName);
        };
    }

    @Override
    public String toTreeString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("FunctionNode(").append(functionName).append(")\n");
        sb.append(argument.toTreeString(indent + "  └─ "));
        return sb.toString();
    }
}
