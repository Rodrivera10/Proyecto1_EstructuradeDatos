public class Main {

    public static void main(String[] args) {

        // 1. El script a ejecutar
        String scriptSig    = "firma pubKey";
        String scriptPubKey = "OP_DUP OP_HASH160 HASH_pubKey OP_EQUALVERIFY OP_CHECKSIG";

        // 2. Juntamos los dos scripts
        String scriptCompleto = scriptSig + " " + scriptPubKey;

        // 3. Creamos el intérprete (true = modo trace activado)
        Interpreter interpreter = new Interpreter(true);

        // 4. Ejecutamos y mostramos resultado
        boolean resultado = interpreter.execute(scriptCompleto);

        if (resultado) {
            System.out.println(" Script VÁLIDO ;) ");
        } else {
            System.out.println(" Script INVÁLIDO");
        }
    }
}