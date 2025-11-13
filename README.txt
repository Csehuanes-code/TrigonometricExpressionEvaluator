# Parser y Evaluador de Expresiones Trigonométricas

## Descripción
Este proyecto implementa un reconocedor descendente (parser recursivo) y evaluador para expresiones aritméticas y trigonométricas basado en una gramática LL(1).

## Gramática LL(1)

\`\`\`
A  -> BA'
A' -> +BA' | -BA' | λ
B  -> CB'
B' -> *CB' | /CB' | λ
C  -> DC'
C' -> ^DC' | λ
D  -> Función(A) | (A) | Letra | Digito
\`\`\`

### Terminales
- Operadores: `+`, `-`, `*`, `/`, `^`
- Paréntesis: `(`, `)`
- Funciones: `sin`, `cos`, `tan` (case-insensitive)
- Dígitos: números reales (ej: 3, 0.5, .2, -2, 2E2, -25.32)
- Variables: letras (a-z, A-Z)

## Estructura del Proyecto

\`\`\`
domine/
├── Token.java          - Clase que representa un token
├── TokenType.java      - Enum con tipos de tokens
├── Dictionary.java     - Mapeo de palabras reservadas
├── Lexer.java          - Analizador léxico (tokenizador)
└── Parser.java         - Analizador sintáctico y evaluador

Main.java               - Programa principal interactivo
Test.java              - Casos de prueba automatizados
\`\`\`

## Componentes

### 1. Lexer (Analizador Léxico)
Convierte la cadena de entrada en una lista de tokens:
- Reconoce números (incluyendo decimales y notación científica)
- Identifica funciones trigonométricas
- Detecta operadores y paréntesis
- Identifica variables

### 2. Parser (Analizador Sintáctico)
Implementación de parser recursivo descendente:
- Sigue la gramática LL(1) definida
- Evalúa la expresión durante el análisis
- Solicita valores para variables cuando es necesario
- Maneja precedencia de operadores correctamente

### 3. Evaluador
Integrado en el parser, evalúa:
- Operaciones aritméticas: `+`, `-`, `*`, `/`, `^`
- Funciones trigonométricas: `Math.sin()`, `Math.cos()`, `Math.tan()`
- Variables simbólicas (solicita valores al usuario)

## Compilación y Ejecución

\`\`\`bash
# Compilar
javac -d bin Main.java Test.java domine/*.java

# Ejecutar programa principal (interactivo)
java -cp bin Main

# Ejecutar casos de prueba
java -cp bin Test
\`\`\`

## Ejemplos de Uso

### Expresiones válidas:
\`\`\`
sin(3.14/2) + cos(0)*2^2       → 5.0
sin(0) + cos(0)                 → 1.0
2^3^2                           → 512.0 (asociatividad derecha)
(3+5)*2                         → 16.0
cos(x)^2 + sin(x)^2            → 1.0 (para x=π/4)
\`\`\`

## Casos de Prueba

El archivo `Test.java` incluye casos de prueba que verifican:
- ✓ Expresiones aritméticas básicas
- ✓ Funciones trigonométricas
- ✓ Precedencia de operadores
- ✓ Potenciación con asociatividad derecha
- ✓ Anidamiento de paréntesis
- ✓ Combinaciones complejas

## Notas Técnicas

- **Precedencia de operadores**: `^` > `*,/` > `+,-`
- **Asociatividad**: Potencia es asociativa por la derecha (2^3^2 = 2^(3^2) = 512)
- **Variables**: Se solicitan valores en tiempo de ejecución
- **Ángulos**: Las funciones trigonométricas esperan radianes
