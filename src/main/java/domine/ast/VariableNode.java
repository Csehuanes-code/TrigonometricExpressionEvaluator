package domine.ast;

import lombok.Getter;
import resources.message.ExpectedMessage;

import java.util.Map;

/**
 * Nodo que representa una variable simbólica
 */
@Getter
public class VariableNode extends ASTNode {
    private final String name;
    private final Map<String, Double> variableValues;

    public VariableNode(String name, Map<String, Double> variableValues) {
        this.name = name;
        this.variableValues = variableValues;
    }

    @Override
    public double evaluate() throws Exception {
        if (!variableValues.containsKey(name)) {
            throw new Exception(ExpectedMessage.unDefinedVariable(name));
        }
        return variableValues.get(name);
    }

    @Override
    public String toTreeString(String indent) {
        return indent + "VariableNode(" + name + ")";
    }
}
