
public class Main {

    public static void main(String[] args) {

        boolean traceActivo = false;

        // Detectar flag --trace desde línea de comandos
        for (String arg : args) {
            if (arg.equals("--trace")) {
                traceActivo = true;
            }
        }

        System.out.println("==============================================");
        System.out.println("   INTÉRPRETE DE BITCOIN SCRIPT — Fase 2    ");
        System.out.println("==============================================\n");

        // ── DEMO 1: P2PKH Correcto ────────────────────────────────────────────────
        System.out.println("── Demo 1: P2PKH Correcto ──");
        String scriptSig1    = "firma pubKey";
        String scriptPubKey1 = "OP_DUP OP_HASH160 HASH_pubKey OP_EQUALVERIFY OP_CHECKSIG";
        runScript(scriptSig1 + " " + scriptPubKey1, traceActivo);

        // ── DEMO 2: P2PKH Incorrecto (hash no coincide) ───────────────────────────
        System.out.println("\n── Demo 2: P2PKH Incorrecto ──");
        String scriptSig2    = "firma pubKeyFalsa";
        String scriptPubKey2 = "OP_DUP OP_HASH160 HASH_pubKeyReal OP_EQUALVERIFY OP_CHECKSIG";
        runScript(scriptSig2 + " " + scriptPubKey2, traceActivo);

        // ── DEMO 3: Condicional OP_IF / OP_ELSE / OP_ENDIF ───────────────────────
        System.out.println("\n── Demo 3: OP_IF / OP_ELSE / OP_ENDIF ──");
        // Implementado por David — se llama a AdvancedInterpreter
        AdvancedInterpreter advInterp = new AdvancedInterpreter(traceActivo);
        String scriptIf = "OP_1 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF";
        boolean resIf = advInterp.execute(scriptIf);
        System.out.println("Script: " + scriptIf);
        System.out.println("Resultado: " + (resIf ? "VÁLIDO ✓" : "INVÁLIDO ✗") + "\n");
    }

    /**
     * Ejecuta un script y muestra el resultado en consola.
     *
     * @param script      el script completo (scriptSig + scriptPubKey)
     * @param traceActivo si es true, muestra el estado de la pila tras cada opcode
     */
    private static void runScript(String script, boolean traceActivo) {
        System.out.println("Script: " + script);
        Interpreter interpreter = new Interpreter(traceActivo);
        try {
            boolean resultado = interpreter.execute(script);
            System.out.println("Resultado: " + (resultado ? "VÁLIDO ✓" : "INVÁLIDO ✗"));
        } catch (RuntimeException e) {
            System.out.println("Error durante ejecución: " + e.getMessage());
            System.out.println("Resultado: INVÁLIDO ✗");
        }
    }
}