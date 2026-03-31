/**
 * Demostración de Pay-to-Public-Key-Hash (P2PKH).
 *
 * <p>P2PKH es el tipo de transacción más común en Bitcoin. El flujo es:</p>
 * <ol>
 *   <li>El receptor publica su hash de clave pública (scriptPubKey).</li>
 *   <li>El emisor proporciona su firma y su clave pública completa (scriptSig).</li>
 *   <li>El intérprete verifica que la clave coincide con el hash y que la firma es válida.</li>
 * </ol>
 *
 * <p>Script estándar P2PKH:</p>
 * <pre>
 *   scriptSig    : &lt;firma&gt; &lt;pubKey&gt;
 *   scriptPubKey : OP_DUP OP_HASH160 &lt;pubKeyHash&gt; OP_EQUALVERIFY OP_CHECKSIG
 * </pre>
 *
 * <p>Convenciones de simulación usadas en esta demo:</p>
 * <ul>
 *   <li>Hash de "Alice"  → "HASH_Alice"</li>
 *   <li>Firma válida de Alice → "SIG_Alice"</li>
 * </ul>
 *
 * @author David 
 * @version 1.0
 */
public class P2PKHDemo {

    /** Separador visual para la consola. */
    private static final String SEP = "=".repeat(60);

    /**
     * Ejecuta los tres escenarios P2PKH: correcto, firma incorrecta y clave incorrecta.
     *
     * @param args argumentos de línea de comandos (no usados)
     */
    public static void main(String[] args) {

        System.out.println(SEP);
        System.out.println("   DEMO P2PKH — Pay-to-Public-Key-Hash");
        System.out.println(SEP);

       
        demostrar(
            "Escenario 1: P2PKH CORRECTO (Alice gasta sus fondos)",
            "SIG_Alice Alice",
            "OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG",
            true  // trace ON
        );

        
        demostrar(
            "Escenario 2: P2PKH INCORRECTO — firma inválida (Bob intenta gastar fondos de Alice)",
            "SIG_Bob Alice",
            "OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG",
            true
        );

        
        demostrar(
            "Escenario 3: P2PKH INCORRECTO — clave pública equivocada (falla en EQUALVERIFY)",
            "SIG_Bob Bob",
            "OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG",
            true
        );
    }

   

    /**
     * Ejecuta un par scriptSig + scriptPubKey e imprime el resultado.
     *
     * @param descripcion texto descriptivo del escenario
     * @param scriptSig   script de desbloqueo (proporcionado por el gastador)
     * @param scriptPubKey script de bloqueo (publicado por el receptor)
     * @param trace       si es {@code true}, muestra la ejecución paso a paso
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
            System.out.println("  >>> RESULTADO: " + (resultado ? " VÁLIDO" : " INVÁLIDO"));
        } catch (RuntimeException e) {
            System.out.println();
            System.out.println("  >>> EXCEPCIÓN: " + e.getMessage());
            System.out.println("  >>> RESULTADO:  INVÁLIDO (script abortado)");
        }

        System.out.println(SEP);
    }
}
