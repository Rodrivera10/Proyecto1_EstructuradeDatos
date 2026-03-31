/**
 * Demostración de OP_CHECKMULTISIG — Multifirma 2 de 3 (simulada).
 *
 * <p>En Bitcoin, una transacción multisig M-de-N requiere que M de N firmantes
 * autorizados proporcionen firmas válidas para gastar los fondos. Esto se usa
 * en carteras compartidas, escrow y contratos inteligentes.</p>
 *
 * <p>Formato del script en la pila (de abajo hacia arriba al momento de ejecutar
 * OP_CHECKMULTISIG):</p>
 * <pre>
 *   &lt;dummy OP_0&gt; &lt;sig1&gt; &lt;sig2&gt; ... &lt;sigM&gt; &lt;M&gt; &lt;pub1&gt; &lt;pub2&gt; ... &lt;pubN&gt; &lt;N&gt;
 *   ─────────────────────────────────────────────────────────────────────────────
 *   tope ↑
 * </pre>
 *
 * <p>scriptSig  (desbloqueo): {@code OP_0 <sig1> <sig2> OP_2}</p>
 * <p>scriptPubKey (bloqueo): {@code <pub1> <pub2> <pub3> OP_3 OP_CHECKMULTISIG}</p>
 *
 * <p>Convención de firma simulada: "SIG_X" es válida para la clave pública "X".</p>
 *
 * @author David 
 * @version 1.0
 */
public class MultisigDemo {

    private static final String SEP = "=".repeat(60);

    /**
     * Punto de entrada: ejecuta los escenarios de multisig 2-de-3.
     *
     * @param args argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {

        System.out.println(SEP);
        System.out.println("   DEMO MULTISIG — OP_CHECKMULTISIG (2 de 3)");
        System.out.println(SEP);

      
        // ── Escenario 1: Alice y Bob firman (válido) ──────────────────────────
        demostrar(
            "Escenario 1: Alice + Bob firman — VÁLIDO (2/3 firmas correctas)",
            // scriptSig: dummy + 2 firmas + número de firmas
            "OP_0 SIG_Alice SIG_Bob OP_2",
            // scriptPubKey: 3 claves públicas + N + OP_CHECKMULTISIG
            "Alice Bob Carlos OP_3 OP_CHECKMULTISIG",
            true
        );

        
        demostrar(
            "Escenario 2: Alice + Carlos firman — VÁLIDO (2/3 firmas correctas)",
            "OP_0 SIG_Alice SIG_Carlos OP_2",
            "Alice Bob Carlos OP_3 OP_CHECKMULTISIG",
            true
        );

        
        demostrar(
            "Escenario 3: Solo 1 firma válida de 2 requeridas — INVÁLIDO",
            "OP_0 SIG_Alice SIG_impostor OP_2",
            "Alice Bob Carlos OP_3 OP_CHECKMULTISIG",
            true
        );

        
        demostrar(
            "Escenario 4: Firmas falsas — INVÁLIDO (SIG_Bob no es válida para Alice)",
            "OP_0 SIG_Bob SIG_Bob OP_2",
            "Alice Bob Carlos OP_3 OP_CHECKMULTISIG",
            true
        );

        
        System.out.println("\nEscenario 5: Sin dummy OP_0 — ERROR esperado (bug histórico)");
        System.out.println("  scriptSig    : SIG_Bob SIG_Carlos OP_2");
        System.out.println("  scriptPubKey : Alice Bob Carlos OP_3 OP_CHECKMULTISIG");
        System.out.println();
        Interpreter interprete = new Interpreter(false);
        try {
            boolean resultado = interprete.execute(
                "SIG_Bob SIG_Carlos OP_2 Alice Bob Carlos OP_3 OP_CHECKMULTISIG");
            System.out.println("  >>> RESULTADO: " + (resultado ? " VÁLIDO" : " INVÁLIDO"));
        } catch (RuntimeException e) {
            System.out.println("  >>> EXCEPCIÓN: " + e.getMessage());
            System.out.println("  >>> RESULTADO:  INVÁLIDO (pila vacía donde se esperaba el dummy)");
        }
        System.out.println(SEP);
    }


    /**
     * Ejecuta un par scriptSig + scriptPubKey de multisig e imprime el resultado.
     *
     * @param descripcion  texto descriptivo del escenario
     * @param scriptSig    script de desbloqueo
     * @param scriptPubKey script de bloqueo
     * @param trace        si es {@code true}, muestra la ejecución paso a paso
     */
    private static void demostrar(String descripcion,
                                   String scriptSig,
                                   String scriptPubKey,
                                   boolean trace) {

        System.out.println("\n" + descripcion);
        System.out.println("  scriptSig    : " + scriptSig);
        System.out.println("  scriptPubKey : " + scriptPubKey);
        System.out.println();

        String scriptCompleto = scriptSig + " " + scriptPubKey;
        Interpreter interprete = new Interpreter(trace);

        try {
            boolean resultado = interprete.execute(scriptCompleto);
            System.out.println();
            System.out.println("  >>> RESULTADO: " + (resultado ? "VÁLIDO" : " INVÁLIDO"));
        } catch (RuntimeException e) {
            System.out.println();
            System.out.println("  >>> EXCEPCIÓN: " + e.getMessage());
            System.out.println("  >>> RESULTADO:  INVÁLIDO");
        }

        System.out.println(SEP);
    }
}
