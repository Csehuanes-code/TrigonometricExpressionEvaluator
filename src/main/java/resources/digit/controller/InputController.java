package resources.digit.controller;

/**
 * Controlador para validar la entrada de cadenas numéricas.
 * Verifica que todos los caracteres de una cadena sean válidos para formar un número.
 */
public class InputController {
    /**
     * Valida que una cadena contenga únicamente caracteres válidos para números.
     * Los caracteres válidos son: dígitos, punto decimal, signos (+/-) y exponentes (e/E).
     *
     * @param chain La cadena a validar
     * @return true si todos los caracteres son válidos, false si hay algún carácter inválido
     */
    public static boolean correctInput(String chain) {
        for(char c: chain.toCharArray()){
            // Verificar que cada carácter sea un exponente, dígito, punto o signo
            if(!(CharacterController.isExponent(c)
                    || Character.isDigit(c) || CharacterController.isPoint(c)
                    || CharacterController.isSign(c))) return false;
        }
        return true;
    }
}
