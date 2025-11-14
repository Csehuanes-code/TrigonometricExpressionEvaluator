# Parser y Evaluador de Expresiones Trigonométricas con AST

## Descripción
Este proyecto implementa un reconocedor descendente (parser recursivo) y evaluador para expresiones aritméticas y trigonométricas basado en una gramática LL(1), con construcción de Árbol de Sintaxis Abstracta (AST).

## Gramática LL(1)

A  -> BA'  
A' -> +BA' | -BA' | λ  
B  -> CB'  
B' -> *CB' | /CB' | λ  
C  -> DC'  
C' -> ^DC' | λ  
D  -> -D | Función(A) | (A) | Letra | Digito


### Terminales
- Operadores: `+`, `-`, `*`, `/`, `^`
- Paréntesis: `(`, `)`
- Funciones: `sen`,`sin`, `cos`, `tan` (case-insensitive)
- Dígitos: números reales (ej: 3, 0.5, .2, -2, 2E2, -25.32)
- Variables: letras (a-z, A-Z)

## Estructura del Proyecto

\`\`\`
domine/  
├── Token.java          - Clase que representa un token  
├── TokenType.java      - Enum con tipos de tokens  
├── Dictionary.java     - Mapeo de palabras reservadas  
├── Lexer.java          - Analizador léxico (tokenizador)  
├── Parser.java         - Analizador sintáctico con construcción de AST
├── Evaluator.java      - Evaluador de AST  
└── ast/   
├── ASTNode.java              - Clase base abstracta para nodos  
├── NumberNode.java           - Nodo para constantes numéricas  
├── VariableNode.java         - Nodo para variables simbólicas  
├── BinaryOperationNode.java  - Nodo para operaciones binarias
└── FunctionNode.java         - Nodo para funciones trigonométricas

Main.java                     - Programa principal interactivo
Test.java                     - Casos de prueba automatizados
TestWithVariables.java        - Pruebas con variables predefinidas
ASTVisualizerDemo.java        - Demostración de visualización del AST
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
- **Construye un Árbol de Sintaxis Abstracta (AST)**
- Solicita valores para variables cuando es necesario
- Maneja precedencia de operadores correctamente

### 3. AST (Árbol de Sintaxis Abstracta)
Representación estructurada de la expresión:
- **NumberNode**: Constantes numéricas
- **VariableNode**: Variables simbólicas (x, y, z)
- **BinaryOperationNode**: Operaciones binarias (+, -, *, /, ^)
- **FunctionNode**: Funciones trigonométricas (sin, cos, tan)

**Ventajas del AST:**
- Separación entre análisis sintáctico y evaluación
- Posibilidad de evaluar múltiples veces con diferentes valores
- Visualización clara de la estructura de la expresión
- Facilita optimizaciones y transformaciones

### 4. Evaluador
Recorre y evalúa el AST:
- Operaciones aritméticas: `+`, `-`, `*`, `/`, `^`
- Funciones trigonométricas: `Math.sin()`, `Math.cos()`, `Math.tan()`
- Variables simbólicas (usa valores previamente definidos)

## Compilación y Ejecución

\`\`\`bash
# Compilar todos los archivos
javac domine/*.java domine/ast/*.java *.java

# Ejecutar programa principal (interactivo con visualización de AST)
java Main

# Ejecutar casos de prueba básicos
java Test

# Ejecutar pruebas con variables
java TestWithVariables

# Ejecutar demostración de visualización del AST
java ASTVisualizerDemo
\`\`\`

## Ejemplos de Uso

### Expresiones válidas:
\`\`\`
sin(3.14/2) + cos(0)*2^2       → 5.0   
sin(0) + cos(0)                 → 1.0  
2^3^2                           → 512.0 (asociatividad derecha)  
(3+5)*2                         → 16.0  
cos(x)^2 + sin(x)^2            → 1.0 (identidad trigonométrica)  
tan(x^2 + sin(x))              → evalúa con valor de x
\`\`\`

### Visualización del AST

Expresión: `sin(x) + 2 * 3`

\`\`\`
BinaryOp(+)  
├─ FunctionNode(sin)
│   └─ VariableNode(x)  
└─ BinaryOp(*)
├─ NumberNode(2.0)
└─ NumberNode(3.0)
\`\`\`

## Casos de Prueba

Los archivos de prueba incluyen:
- ✓ Expresiones aritméticas básicas
- ✓ Funciones trigonométricas
- ✓ Precedencia de operadores
- ✓ Potenciación con asociatividad derecha
- ✓ Anidamiento de paréntesis
- ✓ Variables simbólicas
- ✓ Identidades matemáticas
- ✓ Combinaciones complejas

## Flujo de Ejecución

1. **Tokenización** (Lexer): `"sin(x)+2"` → `[SIN, LPARENT, VARIABLE(x), RPARENT, PLUS, DIGIT(2)]`
2. **Parsing** (Parser): Tokens → AST
3. **Solicitud de variables**: Si hay variables, se piden sus valores
4. **Evaluación** (Evaluator): AST → Resultado numérico

## Notas Técnicas

- **Precedencia de operadores**: `^` > `*,/` > `+,-`
- **Asociatividad**: Potencia es asociativa por la derecha (2^3^2 = 2^(3^2) = 512)
- **Variables**: Se solicitan valores antes de la evaluación
- **Ángulos**: Las funciones trigonométricas esperan radianes
- **AST**: Permite reutilización y optimización antes de evaluar
