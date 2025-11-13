package resources.digit.automaton;

import resources.digit.controller.CharacterController;
import resources.digit.transition.State;
import resources.digit.transition.TransitionMatrix;

/**
 * Autómata finito para validar números en lenguaje C.
 * Reconoce números enteros, decimales y notación científica (con exponente).
 * Utiliza una matriz de transición de estados para procesar cada carácter de la entrada.
 */
public class DigitAutomaton {
    /**
     * Valida si una cadena representa un número válido en C.
     * Procesa cada carácter usando la matriz de transición y verifica que el estado final sea aceptable.
     *
     * @param chain La cadena a validar
     * @return true si es un número válido, false en caso contrario
     */
    public static boolean isCorrectDigit(String chain) {
        int lastIndex = 0; // Comenzar en el estado inicial

        // Procesar cada carácter de la cadena
        for (char c : chain.toCharArray()) {
            // Obtener el índice de columna según el tipo de carácter
            int columnIndex = CharacterController.indexByCharacter(c);
            // Transicionar al siguiente estado
            lastIndex = TransitionMatrix.getInstance().matrix[lastIndex][columnIndex].getIndex();
        }

        // Verificar si el estado final es un estado de aceptación
        if(State.getEstadoPorIndice(lastIndex).getIsAceptable()){
            try {
                Double.parseDouble(chain);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }
}
