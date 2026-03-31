import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Intérprete de un subconjunto de Bitcoin Script.
 *
 * <p>Ejecuta scripts combinados (scriptSig + scriptPubKey) sobre una pila principal
 * de valores en formato String. Los tokens desconocidos se tratan como datos literales
 * y se empujan directamente a la pila.</p>
 *
 * <p><b>Integrante 1</b> implementó: literales, operaciones de pila, comparaciones,
 * booleanos, aritmética básica y los opcodes criptográficos simulados base.</p>
 *
 * <p><b>Integrante 2</b> añadió: control de flujo condicional (OP_IF / OP_NOTIF /
 * OP_ELSE / OP_ENDIF) con soporte de anidamiento, OP_VERIFY, OP_RETURN,
 * opcodes aritméticos faltantes, verificación de firma simulada realista y
 * OP_CHECKMULTISIG / OP_CHECKMULTISIGVERIFY.</p>
 *
 * @author Integrante 1 (core), Integrante 2 (flujo avanzado y multisig)
 * @version 2.0
 */
public class Interpreter {

    
    private ScriptStack stack;

    
    private boolean trace;

    private Deque<Boolean> ifStack;



    /**
     * Crea un intérprete nuevo con la pila limpia.
     *
     * @param trace {@code true} para activar el modo trace (--trace)
     */
    public Interpreter(boolean trace) {
        this.stack   = new ScriptStack();
        this.trace   = trace;
        this.ifStack = new ArrayDeque<>();
    }

    
      @return 
     
    private boolean isExecuting() {
        for (Boolean b : ifStack) {
            if (!b) return false;
        }
        return true;
    }

    
      @return {@code true} 
    private boolean isParentExecuting() {
        if (ifStack.size() <= 1) return true;
        Iterator<Boolean> it = ifStack.iterator();
        it.next(); 
        while (it.hasNext()) {
            if (!it.next()) return false;
        }
        return true;
    }

  
    /**
     * Simula la verificación criptográfica de una firma contra una clave pública.
     *
     * <p>Convenio de simulación: una firma es válida para una clave pública
     * si y solo si {@code sig.equals("SIG_" + pubkey)}.
     * Por ejemplo, "SIG_Alice" es válida contra "Alice".</p>
     *
     * @param sig    firma (simulada)
     * @param pubkey clave pública (simulada)
     * @return {@code true} si la firma es válida para esa clave
     */
    private boolean mockVerifySig(String sig, String pubkey) {
        return sig.equals("SIG_" + pubkey);
    }

    /**
     * Ejecuta un script Bitcoin (scriptSig + scriptPubKey concatenados).
     *
     * <p>El script se tokeniza por espacios y cada token se procesa de izquierda
     * a derecha. El resultado es {@code true} si al finalizar la pila no está
     * vacía y el tope es un valor distinto de "0".</p>
     *
     * @param script script completo a ejecutar
     * @return {@code true} si el script es válido, {@code false} en caso contrario
     * @throws RuntimeException si ocurre un error irrecuperable (pila vacía, VERIFY fallido, etc.)
     */
    public boolean execute(String script) {

        // Limpiar estado entre ejecuciones consecutivas
        ifStack.clear();

        // 1. Tokenizar el script
        Tokenizer tokenizer = new Tokenizer();
        String[] tokens = tokenizer.tokenize(script);

        // 2. Recorrer cada token uno por uno
        for (String token : tokens) {

         
            switch (token) {

                case "OP_IF": {
                    if (isExecuting()) {
                        String top = stack.pop();
                        ifStack.push(!top.equals("0")); // true = ejecutar rama THEN
                    } else {
                        // Dentro de una rama inactiva: el IF anidado queda inactivo
                        ifStack.push(false);
                    }
                    if (trace) printTrace(token);
                    continue; // saltar al siguiente token
                }

                case "OP_NOTIF": {
                    if (isExecuting()) {
                        String top = stack.pop();
                        ifStack.push(top.equals("0")); // true = ejecutar rama THEN
                    } else {
                        ifStack.push(false);
                    }
                    if (trace) printTrace(token);
                    continue;
                }

                
                case "OP_ELSE": {
                    if (!ifStack.isEmpty() && isParentExecuting()) {
                        boolean current = ifStack.pop();
                        ifStack.push(!current); // cambiar de THEN a ELSE o viceversa
                    }
                    if (trace) printTrace(token);
                    continue;
                }

                
                case "OP_ENDIF": {
                    if (!ifStack.isEmpty()) {
                        ifStack.pop();
                    }
                    if (trace) printTrace(token);
                    continue;
                }
            }

            
            if (!isExecuting()) {
                if (trace) System.out.println("[SKIP] " + token);
                continue;
            }

           

            switch (token) {

                
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

                
                case "OP_DUP": {
                    String tope = stack.peek();
                    stack.push(tope);
                    break;
                }

               
                case "OP_DROP":
                    stack.pop();
                    break;

               
                case "OP_SWAP": {
                    String primero = stack.pop();
                    String segundo = stack.pop();
                    stack.push(segundo);
                    stack.push(primero);
                    break;
                }

               
                case "OP_OVER": {
                    String primero = stack.pop();
                    String segundo = stack.peek();
                    stack.push(primero);
                    stack.push(segundo);
                    break;
                }

               
                case "OP_EQUAL": {
                    String primero = stack.pop();
                    String segundo = stack.pop();
                    stack.push(primero.equals(segundo) ? "1" : "0");
                    break;
                }

                
                case "OP_EQUALVERIFY": {
                    String primero = stack.pop();
                    String segundo = stack.pop();
                    if (!primero.equals(segundo)) {
                        throw new RuntimeException(
                            "Error: OP_EQUALVERIFY falló — valores distintos: \""
                            + primero + "\" vs \"" + segundo + "\"");
                    }
                    break;
                }

                /** OP_NOT: invierte el tope. "0" → "1"; cualquier otro valor → "0". */
                case "OP_NOT": {
                    String valor = stack.pop();
                    stack.push(valor.equals("0") ? "1" : "0");
                    break;
                }

                /** OP_BOOLAND: "1" si ambos topes son ≠ "0". */
                case "OP_BOOLAND": {
                    String primero = stack.pop();
                    String segundo = stack.pop();
                    stack.push((!primero.equals("0") && !segundo.equals("0")) ? "1" : "0");
                    break;
                }

                /** OP_BOOLOR: "1" si al menos uno de los dos topes es ≠ "0". */
                case "OP_BOOLOR": {
                    String primero = stack.pop();
                    String segundo = stack.pop();
                    stack.push((!primero.equals("0") || !segundo.equals("0")) ? "1" : "0");
                    break;
                }

                // ── GRUPO 4: Aritmética ────────────────────────────────────────

                /** OP_ADD: suma los dos topes y empuja el resultado. */
                case "OP_ADD": {
                    int a = Integer.parseInt(stack.pop());
                    int b = Integer.parseInt(stack.pop());
                    stack.push(String.valueOf(a + b));
                    break;
                }

                /**
                 * OP_SUB: resta el tope al segundo elemento (segundo - tope).
                 
                 */
                case "OP_SUB": {
                    int tope    = Integer.parseInt(stack.pop());
                    int segundo = Integer.parseInt(stack.pop());
                    stack.push(String.valueOf(segundo - tope));
                    break;
                }

                /**
                 * OP_NUMEQUALVERIFY: verifica que los dos topes sean iguales como enteros;
                 * aborta si no lo son. 
                 */
                case "OP_NUMEQUALVERIFY": {
                    int a = Integer.parseInt(stack.pop());
                    int b = Integer.parseInt(stack.pop());
                    if (a != b) {
                        throw new RuntimeException(
                            "Error: OP_NUMEQUALVERIFY falló — " + a + " ≠ " + b);
                    }
                    break;
                }

                /**
                 * OP_LESSTHAN: empuja "1" si segundo < tope, "0" si no.
                 
                 */
                case "OP_LESSTHAN": {
                    int tope    = Integer.parseInt(stack.pop());
                    int segundo = Integer.parseInt(stack.pop());
                    stack.push(segundo < tope ? "1" : "0");
                    break;
                }

                /**
                 * OP_GREATERTHAN: empuja "1" si segundo > tope, "0" si no.
                 
                 */
                case "OP_GREATERTHAN": {
                    int tope    = Integer.parseInt(stack.pop());
                    int segundo = Integer.parseInt(stack.pop());
                    stack.push(segundo > tope ? "1" : "0");
                    break;
                }

                /**
                 * OP_LESSTHANOREQUAL: empuja "1" si segundo ≤ tope.
                 * (David)
                 */
                case "OP_LESSTHANOREQUAL": {
                    int tope    = Integer.parseInt(stack.pop());
                    int segundo = Integer.parseInt(stack.pop());
                    stack.push(segundo <= tope ? "1" : "0");
                    break;
                }

                /**
                 * OP_GREATERTHANOREQUAL: empuja "1" si segundo ≥ tope.
                 * (David)
                 */
                case "OP_GREATERTHANOREQUAL": {
                    int tope    = Integer.parseInt(stack.pop());
                    int segundo = Integer.parseInt(stack.pop());
                    stack.push(segundo >= tope ? "1" : "0");
                    break;
                }

               
                case "OP_VERIFY": {
                    String top = stack.pop();
                    if (top.equals("0")) {
                        throw new RuntimeException("Error: OP_VERIFY falló — tope era falso");
                    }
                    break;
                }

               
                case "OP_RETURN":
                    throw new RuntimeException("Error: OP_RETURN — script terminado como inválido");

                // ── GRUPO 6: Criptografía simulada ─────────────────────────────

                /**
                 * OP_SHA256: simula SHA-256 anteponiendo el prefijo "SHA256_" al valor.
                 * En producción se usaría MessageDigest.getInstance("SHA-256").
                 */
                case "OP_SHA256": {
                    String valor = stack.pop();
                    stack.push("SHA256_" + valor);
                    break;
                }

                /**
                 * OP_HASH160: simula HASH160 (SHA-256 + RIPEMD-160) anteponiendo "HASH_".
                 * Es el hash usado en las direcciones P2PKH estándar de Bitcoin.
                 */
                case "OP_HASH160": {
                    String valor = stack.pop();
                    stack.push("HASH_" + valor);
                    break;
                }

                /**
                 * OP_HASH256: simula HASH256 (doble SHA-256) anteponiendo "HASH256_".
                 */
                case "OP_HASH256": {
                    String valor = stack.pop();
                    stack.push("HASH256_" + valor);
                    break;
                }

               
                case "OP_CHECKSIG": {
                    String pubkey = stack.pop();
                    String sig    = stack.pop();
                    stack.push(mockVerifySig(sig, pubkey) ? "1" : "0");
                    break;
                }

                /**
                 * OP_CHECKSIGVERIFY: como OP_CHECKSIG pero aborta si la firma es inválida.
                 * (Integrante 2)
                 */
                case "OP_CHECKSIGVERIFY": {
                    String pubkey = stack.pop();
                    String sig    = stack.pop();
                    if (!mockVerifySig(sig, pubkey)) {
                        throw new RuntimeException(
                            "Error: OP_CHECKSIGVERIFY falló — firma inválida para clave \""
                            + pubkey + "\"");
                    }
                    break;
                }

              
                case "OP_CHECKMULTISIG": {
                    // Leer N (cantidad de pubkeys)
                    int n = Integer.parseInt(stack.pop());
                    String[] pubkeys = new String[n];
                    for (int i = 0; i < n; i++) {
                        pubkeys[i] = stack.pop();
                    }

                    // Leer M (cantidad de firmas requeridas)
                    int m = Integer.parseInt(stack.pop());
                    String[] sigs = new String[m];
                    for (int i = 0; i < m; i++) {
                        sigs[i] = stack.pop();
                    }

              
                    stack.pop();

                    
                    int validadas  = 0;
                    int pubIdx     = 0;

                    for (int sigIdx = 0; sigIdx < m && pubIdx < n; sigIdx++) {
                        while (pubIdx < n) {
                            if (mockVerifySig(sigs[sigIdx], pubkeys[pubIdx])) {
                                validadas++;
                                pubIdx++; // la clave ya fue usada, avanzar
                                break;
                            }
                            pubIdx++; // esta clave no sirve, probar la siguiente
                        }
                    }

                    stack.push(validadas >= m ? "1" : "0");
                    break;
                }

                
                case "OP_CHECKMULTISIGVERIFY": {
                    // Reutilizamos la lógica de CHECKMULTISIG y luego verificamos
                    int n = Integer.parseInt(stack.pop());
                    String[] pubkeys = new String[n];
                    for (int i = 0; i < n; i++) pubkeys[i] = stack.pop();

                    int m = Integer.parseInt(stack.pop());
                    String[] sigs = new String[m];
                    for (int i = 0; i < m; i++) sigs[i] = stack.pop();

                    stack.pop(); // dummy

                    int validadas = 0;
                    int pubIdx = 0;
                    for (int sigIdx = 0; sigIdx < m && pubIdx < n; sigIdx++) {
                        while (pubIdx < n) {
                            if (mockVerifySig(sigs[sigIdx], pubkeys[pubIdx])) {
                                validadas++;
                                pubIdx++;
                                break;
                            }
                            pubIdx++;
                        }
                    }

                    if (validadas < m) {
                        throw new RuntimeException(
                            "Error: OP_CHECKMULTISIGVERIFY falló — solo "
                            + validadas + " de " + m + " firmas válidas");
                    }
                    break;
                }

                // ── DEFAULT: datos literales ────────────────────────────────────
                default:
                    stack.push(token); 
                    break;

            } 

            if (trace) printTrace(token);

        } 

        // Verificar integridad del ifStack (todos los IF deben tener su ENDIF)
        if (!ifStack.isEmpty()) {
            throw new RuntimeException(
                "Error: script mal formado — " + ifStack.size()
                + " bloque(s) OP_IF sin OP_ENDIF correspondiente");
        }

        // Resultado: la pila no debe estar vacía y el tope debe ser verdadero (≠ "0")
        if (stack.isEmpty()) return false;
        return !stack.peek().equals("0");
    }

  

    /**
     * Imprime el estado actual de la pila junto con el opcode que se acaba de ejecutar.
     * Si hay condicionales activos, también muestra el ifStack.
     *
     * @param token opcode o dato recién procesado
     */
    private void printTrace(String token) {
        String ifInfo = ifStack.isEmpty() ? "" : " | ifStack: " + ifStack;
        System.out.println(token + " -> pila: " + stack.toString() + ifInfo);
    }
}
