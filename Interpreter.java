/**
 * Intérprete principal del subconjunto de Bitcoin Script.
 *
 * <p>Ejecuta instrucciones (opcodes) de izquierda a derecha sobre una pila {@link ScriptStack}.
 * La validación del script es exitosa si ninguna instrucción falla y la cima de la pila
 * final contiene un valor verdadero (diferente de "0" y no vacío).</p>
 *
 * <p>Soporta los siguientes grupos de opcodes:</p>
 * <ul>
 *   <li><b>Literales:</b> OP_0/OP_FALSE, OP_1/OP_TRUE ... OP_16</li>
 *   <li><b>Pila:</b> OP_DUP, OP_DROP, OP_SWAP, OP_OVER</li>
 *   <li><b>Comparación:</b> OP_EQUAL, OP_EQUALVERIFY</li>
 *   <li><b>Lógica:</b> OP_NOT, OP_BOOLAND, OP_BOOLOR</li>
 *   <li><b>Aritmética:</b> OP_ADD</li>
 *   <li><b>Cripto simulado:</b> OP_HASH160, OP_CHECKSIG</li>
 *   <li><b>Control de flujo:</b> OP_IF, OP_ELSE, OP_ENDIF (implementados por David)</li>
 *   <li><b>Multisig:</b> OP_CHECKMULTISIG (implementado por David)</li>
 * </ul>
 *
 * <p>Autor: Rodrigo Rivera</p>
 * @version 2.0
 */
public class Interpreter {

    /** Pila principal del intérprete. */
    private ScriptStack stack;

    /** Indica si el modo trace está activado (muestra el estado de la pila tras cada opcode). */
    private boolean trace;

    /**
     * Crea un nuevo intérprete.
     *
     * @param trace {@code true} para activar el modo trace (debug paso a paso)
     */
    public Interpreter(boolean trace) {
        this.stack = new ScriptStack();
        this.trace = trace;
    }

    /**
     * Ejecuta un script de Bitcoin Script.
     *
     * <p>El script se tokeniza y cada token se procesa en orden. Si algún opcode
     * falla (p.ej. OP_EQUALVERIFY con valores distintos, o pop sobre pila vacía),
     * se lanza una {@link RuntimeException} con mensaje descriptivo.</p>
     *
     * @param script el script completo como cadena de texto
     * @return {@code true} si el script es válido (cima de la pila verdadera al final),
     *         {@code false} si la pila queda vacía o la cima es "0"
     */
    public boolean execute(String script) {

        // 1. Tokenizar el script
        Tokenizer tokenizer = new Tokenizer();
        String[] tokens = tokenizer.tokenize(script);

        // 2. Recorrer cada token y ejecutarlo
        for (String token : tokens) {
            executeToken(token);

            // Mostrar estado de la pila si trace está activo
            if (trace) {
                System.out.println("[TRACE] " + token + " -> pila: " + stack.toString());
            }
        }

        // 3. Validar resultado final
        if (stack.isEmpty()) {
            return false;
        }
        return !stack.peek().equals("0");
    }

    /**
     * Procesa un único token (opcode o dato literal).
     *
     * @param token el token a ejecutar
     */
    private void executeToken(String token) {
        switch (token) {

            // ── GRUPO 1: Literales ─────────────────────────────────────────────────
            case "OP_0":
            case "OP_FALSE":
                stack.push("0");
                break;

            case "OP_1":
            case "OP_TRUE":
                stack.push("1");
                break;

            case "OP_2":  stack.push("2");  break;
            case "OP_3":  stack.push("3");  break;
            case "OP_4":  stack.push("4");  break;
            case "OP_5":  stack.push("5");  break;
            case "OP_6":  stack.push("6");  break;
            case "OP_7":  stack.push("7");  break;
            case "OP_8":  stack.push("8");  break;
            case "OP_9":  stack.push("9");  break;
            case "OP_10": stack.push("10"); break;
            case "OP_11": stack.push("11"); break;
            case "OP_12": stack.push("12"); break;
            case "OP_13": stack.push("13"); break;
            case "OP_14": stack.push("14"); break;
            case "OP_15": stack.push("15"); break;
            case "OP_16": stack.push("16"); break;

            // ── GRUPO 2: Operaciones de pila ──────────────────────────────────────

            /**
             * OP_DUP: Duplica el elemento en la cima de la pila.
             * Antes: [a, ...] → Después: [a, a, ...]
             */
            case "OP_DUP": {
                String tope = stack.peek();
                stack.push(tope);
                break;
            }

            /**
             * OP_DROP: Elimina el elemento en la cima de la pila.
             * Antes: [a, ...] → Después: [...]
             */
            case "OP_DROP":
                stack.pop();
                break;

            /**
             * OP_SWAP: Intercambia los dos elementos de arriba de la pila.
             * Antes: [a, b, ...] → Después: [b, a, ...]
             */
            case "OP_SWAP": {
                String primero = stack.pop();
                String segundo = stack.pop();
                stack.push(primero);
                stack.push(segundo);
                break;
            }

            /**
             * OP_OVER: Copia el segundo elemento y lo pone arriba.
             * Antes: [a, b, ...] → Después: [b, a, b, ...]
             */
            case "OP_OVER": {
                String primero = stack.pop();
                String segundo = stack.peek();
                stack.push(primero);
                stack.push(segundo);
                break;
            }

            // ── GRUPO 3: Comparación ──────────────────────────────────────────────

            /**
             * OP_EQUAL: Compara los dos elementos de arriba.
             * Mete "1" si son iguales, "0" si no.
             */
            case "OP_EQUAL": {
                String primero = stack.pop();
                String segundo = stack.pop();
                stack.push(primero.equals(segundo) ? "1" : "0");
                break;
            }

            /**
             * OP_EQUALVERIFY: Compara los dos elementos de arriba.
             * Si son iguales continúa; si NO son iguales, ABORTA con excepción.
             */
            case "OP_EQUALVERIFY": {
                String primero = stack.pop();
                String segundo = stack.pop();
                if (!primero.equals(segundo)) {
                    throw new RuntimeException(
                        "OP_EQUALVERIFY falló: '" + primero + "' != '" + segundo + "'"
                    );
                }
                break;
            }

            // ── GRUPO 4: Lógica booleana ──────────────────────────────────────────

            /**
             * OP_NOT: Invierte el valor lógico del tope.
             * "0" → "1", cualquier otro valor → "0".
             */
            case "OP_NOT": {
                String valor = stack.pop();
                stack.push(valor.equals("0") ? "1" : "0");
                break;
            }

            /**
             * OP_BOOLAND: AND lógico. Mete "1" solo si ambos son distintos de "0".
             */
            case "OP_BOOLAND": {
                String primero = stack.pop();
                String segundo = stack.pop();
                boolean resultado = !primero.equals("0") && !segundo.equals("0");
                stack.push(resultado ? "1" : "0");
                break;
            }

            /**
             * OP_BOOLOR: OR lógico. Mete "1" si al menos uno es distinto de "0".
             */
            case "OP_BOOLOR": {
                String primero = stack.pop();
                String segundo = stack.pop();
                boolean resultado = !primero.equals("0") || !segundo.equals("0");
                stack.push(resultado ? "1" : "0");
                break;
            }

            // ── GRUPO 5: Aritmética ───────────────────────────────────────────────

            /**
             * OP_ADD: Suma los dos elementos de arriba (como enteros).
             * Lanza excepción si alguno no es un número válido.
             */
            case "OP_ADD": {
                String primero = stack.pop();
                String segundo = stack.pop();
                try {
                    int resultado = Integer.parseInt(primero) + Integer.parseInt(segundo);
                    stack.push(String.valueOf(resultado));
                } catch (NumberFormatException e) {
                    throw new RuntimeException(
                        "OP_ADD falló: '" + primero + "' o '" + segundo + "' no son números enteros"
                    );
                }
                break;
            }

            // ── GRUPO 6: Criptografía simulada ───────────────────────────────────

            /**
             * OP_HASH160: Simula HASH160 (SHA-256 + RIPEMD-160).
             * En esta implementación, agrega el prefijo "HASH_" al valor.
             */
            case "OP_HASH160": {
                String valor = stack.pop();
                stack.push("HASH_" + valor);
                break;
            }

            /**
             * OP_CHECKSIG: Simula verificación de firma digital.
             * Saca pubKey y firma de la pila, y mete "1" (firma siempre válida en simulación).
             */
            case "OP_CHECKSIG": {
                stack.pop(); // saca pubKey
                stack.pop(); // saca firma
                stack.push("1"); // simula firma válida
                break;
            }

            // ── GRUPO 7: Control de flujo (implementados por David) ───────────────
            // OP_IF, OP_ELSE, OP_ENDIF, OP_CHECKMULTISIG → ver AdvancedOpcodes.java

            default:
                // Cualquier token no reconocido como opcode se trata como dato literal
                stack.push(token);
                break;
        }
    }

    /**
     * Permite acceder a la pila interna del intérprete (útil para pruebas unitarias).
     *
     * @return referencia a la pila actual
     */
    public ScriptStack getStack() {
        return stack;
    }
}