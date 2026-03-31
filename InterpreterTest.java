import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


   @author 
   @version 1.0
 
public class InterpreterTest {

    private boolean run(String script) {
        return new Interpreter(false).execute(script);
    }

    private RuntimeException runFail(String script) {
        return assertThrows(RuntimeException.class, () -> new Interpreter(false).execute(script));
    }

    @Test
    @DisplayName("OP_0 empuja falso")
    void testOp0False() {
        assertFalse(run("OP_0"));
    }

    @Test
    @DisplayName("OP_FALSE funciona igual que OP_0")
    void testOpFalseAlias() {
        assertFalse(run("OP_FALSE"));
    }

    @Test
    @DisplayName("OP_1 empuja verdadero")
    void testOp1True() {
        assertTrue(run("OP_1"));
    }

    @Test
    @DisplayName("OP_TRUE funciona igual que OP_1")
    void testOpTrueAlias() {
        assertTrue(run("OP_TRUE"));
    }

    @Test
    @DisplayName("Constantes numéricas OP_2 a OP_16 se pueden empujar sin error")
    void testNumericOpcodes2To16() {
        assertTrue(run(
            "OP_2 OP_DROP OP_3 OP_DROP OP_4 OP_DROP OP_5 OP_DROP OP_6 OP_DROP " +
            "OP_7 OP_DROP OP_8 OP_DROP OP_9 OP_DROP OP_10 OP_DROP OP_11 OP_DROP " +
            "OP_12 OP_DROP OP_13 OP_DROP OP_14 OP_DROP OP_15 OP_DROP OP_16"
        ));
    }

    @Test
    @DisplayName("Datos literales se empujan como tokens normales")
    void testLiteralPush() {
        assertTrue(run("hola"));
    }

    @Test
    @DisplayName("OP_DUP duplica la cima")
    void testOpDup() {
        assertTrue(run("dato OP_DUP OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_DROP elimina la cima")
    void testOpDrop() {
        assertTrue(run("A B OP_DROP"));
    }

    @Test
    @DisplayName("OP_SWAP intercambia los dos elementos superiores")
    void testOpSwap() {
        assertTrue(run("A B OP_SWAP A OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_OVER copia el segundo elemento a la cima")
    void testOpOver() {
        assertTrue(run("A B OP_OVER A OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_EQUAL devuelve verdadero con valores iguales")
    void testOpEqualTrue() {
        assertTrue(run("abc abc OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_EQUAL devuelve falso con valores distintos")
    void testOpEqualFalse() {
        assertFalse(run("abc xyz OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_EQUALVERIFY pasa cuando ambos valores son iguales")
    void testOpEqualVerifySuccess() {
        assertTrue(run("clave clave OP_EQUALVERIFY OP_1"));
    }

    @Test
    @DisplayName("OP_EQUALVERIFY falla cuando los valores son distintos")
    void testOpEqualVerifyFail() {
        RuntimeException ex = runFail("clave otra OP_EQUALVERIFY");
        assertTrue(ex.getMessage().contains("OP_EQUALVERIFY"));
    }

    @Test
    @DisplayName("OP_NOT invierte 0 a 1")
    void testOpNotOnZero() {
        assertTrue(run("OP_0 OP_NOT"));
    }

    @Test
    @DisplayName("OP_NOT invierte cualquier valor distinto de 0 a 0")
    void testOpNotOnNonZero() {
        assertFalse(run("OP_5 OP_NOT"));
    }

    @Test
    @DisplayName("OP_BOOLAND solo es verdadero si ambos operandos son verdaderos")
    void testOpBoolAnd() {
        assertTrue(run("OP_1 OP_2 OP_BOOLAND"));
        assertFalse(run("OP_1 OP_0 OP_BOOLAND"));
    }

    @Test
    @DisplayName("OP_BOOLOR es verdadero si al menos un operando es verdadero")
    void testOpBoolOr() {
        assertTrue(run("OP_0 OP_8 OP_BOOLOR"));
        assertFalse(run("OP_0 OP_0 OP_BOOLOR"));
    }

    @Test
    @DisplayName("OP_ADD suma los dos enteros superiores")
    void testOpAdd() {
        assertTrue(run("2 3 OP_ADD 5 OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_SUB resta segundo - cima")
    void testOpSub() {
        assertTrue(run("8 3 OP_SUB 5 OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_NUMEQUALVERIFY pasa cuando ambos enteros son iguales")
    void testOpNumEqualVerifySuccess() {
        assertTrue(run("7 7 OP_NUMEQUALVERIFY OP_1"));
    }

    @Test
    @DisplayName("OP_NUMEQUALVERIFY falla cuando los enteros son distintos")
    void testOpNumEqualVerifyFail() {
        RuntimeException ex = runFail("7 8 OP_NUMEQUALVERIFY");
        assertTrue(ex.getMessage().contains("OP_NUMEQUALVERIFY"));
    }

    @Test
    @DisplayName("OP_LESSTHAN compara correctamente")
    void testOpLessThan() {
        assertTrue(run("2 5 OP_LESSTHAN"));
        assertFalse(run("8 3 OP_LESSTHAN"));
    }

    @Test
    @DisplayName("OP_GREATERTHAN compara correctamente")
    void testOpGreaterThan() {
        assertTrue(run("8 3 OP_GREATERTHAN"));
        assertFalse(run("2 5 OP_GREATERTHAN"));
    }

    @Test
    @DisplayName("OP_LESSTHANOREQUAL compara correctamente")
    void testOpLessThanOrEqual() {
        assertTrue(run("5 5 OP_LESSTHANOREQUAL"));
        assertTrue(run("2 5 OP_LESSTHANOREQUAL"));
        assertFalse(run("9 5 OP_LESSTHANOREQUAL"));
    }

    @Test
    @DisplayName("OP_GREATERTHANOREQUAL compara correctamente")
    void testOpGreaterThanOrEqual() {
        assertTrue(run("5 5 OP_GREATERTHANOREQUAL"));
        assertTrue(run("9 5 OP_GREATERTHANOREQUAL"));
        assertFalse(run("2 5 OP_GREATERTHANOREQUAL"));
    }

    @Test
    @DisplayName("OP_VERIFY pasa con valor verdadero")
    void testOpVerifySuccess() {
        assertTrue(run("OP_1 OP_VERIFY OP_1"));
    }

    @Test
    @DisplayName("OP_VERIFY falla con valor falso")
    void testOpVerifyFail() {
        RuntimeException ex = runFail("OP_0 OP_VERIFY");
        assertTrue(ex.getMessage().contains("OP_VERIFY"));
    }

    @Test
    @DisplayName("OP_RETURN invalida inmediatamente el script")
    void testOpReturnFail() {
        RuntimeException ex = runFail("OP_1 OP_RETURN");
        assertTrue(ex.getMessage().contains("OP_RETURN"));
    }

    @Test
    @DisplayName("OP_SHA256 simula el hash correctamente")
    void testOpSha256() {
        assertTrue(run("secreto OP_SHA256 SHA256_secreto OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_HASH160 simula el hash correctamente")
    void testOpHash160() {
        assertTrue(run("Alice OP_HASH160 HASH_Alice OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_HASH256 simula el doble hash correctamente")
    void testOpHash256() {
        assertTrue(run("dato OP_HASH256 HASH256_dato OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_CHECKSIG acepta una firma válida")
    void testOpCheckSigSuccess() {
        assertTrue(run("SIG_Alice Alice OP_CHECKSIG"));
    }

    @Test
    @DisplayName("OP_CHECKSIG rechaza una firma inválida")
    void testOpCheckSigFail() {
        assertFalse(run("SIG_Bob Alice OP_CHECKSIG"));
    }

    @Test
    @DisplayName("OP_CHECKSIGVERIFY pasa con firma válida")
    void testOpCheckSigVerifySuccess() {
        assertTrue(run("SIG_Alice Alice OP_CHECKSIGVERIFY OP_1"));
    }

    @Test
    @DisplayName("OP_CHECKSIGVERIFY falla con firma inválida")
    void testOpCheckSigVerifyFail() {
        RuntimeException ex = runFail("SIG_Bob Alice OP_CHECKSIGVERIFY");
        assertTrue(ex.getMessage().contains("OP_CHECKSIGVERIFY"));
    }

    @Test
    @DisplayName("OP_IF ejecuta la rama THEN cuando la condición es verdadera")
    void testOpIfThenBranch() {
        assertTrue(run("OP_1 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF 2 OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_IF ejecuta la rama ELSE cuando la condición es falsa")
    void testOpIfElseBranch() {
        assertTrue(run("OP_0 OP_IF OP_2 OP_ELSE OP_3 OP_ENDIF 3 OP_EQUAL"));
    }

    @Test
    @DisplayName("OP_NOTIF ejecuta la rama THEN cuando la condición es falsa")
    void testOpNotIfBranch() {
        assertTrue(run("OP_0 OP_NOTIF OP_9 OP_ELSE OP_4 OP_ENDIF 9 OP_EQUAL"));
    }

    @Test
    @DisplayName("Condicionales anidados funcionan correctamente")
    void testNestedConditionals() {
        assertTrue(run("OP_1 OP_IF OP_1 OP_IF OP_5 OP_ELSE OP_6 OP_ENDIF OP_ELSE OP_7 OP_ENDIF 5 OP_EQUAL"));
    }

    @Test
    @DisplayName("IF externo falso hace skip completo del IF interno")
    void testNestedConditionalsSkippedInnerBlock() {
        assertTrue(run("OP_0 OP_IF OP_1 OP_IF OP_9 OP_ELSE OP_8 OP_ENDIF OP_ELSE OP_3 OP_ENDIF 3 OP_EQUAL"));
    }

    @Test
    @DisplayName("Script con IF sin ENDIF falla por estructura inválida")
    void testMalformedIfWithoutEndIf() {
        RuntimeException ex = runFail("OP_1 OP_IF OP_2");
        assertTrue(ex.getMessage().contains("OP_IF sin OP_ENDIF"));
    }

    @Test
    @DisplayName("P2PKH correcto valida firma y hash de clave")
    void testP2PKHSuccess() {
        String script = "SIG_Alice Alice OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG";
        assertTrue(run(script));
    }

    @Test
    @DisplayName("P2PKH incorrecto falla por firma inválida")
    void testP2PKHWrongSignature() {
        String script = "SIG_Bob Alice OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG";
        assertFalse(run(script));
    }

    @Test
    @DisplayName("P2PKH incorrecto falla por clave pública equivocada")
    void testP2PKHWrongPubKey() {
        String script = "SIG_Bob Bob OP_DUP OP_HASH160 HASH_Alice OP_EQUALVERIFY OP_CHECKSIG";
        RuntimeException ex = runFail(script);
        assertTrue(ex.getMessage().contains("OP_EQUALVERIFY"));
    }

    @Test
    @DisplayName("HTLC simplificado con preimagen correcta resulta válido")
    void testHtlcStyleSuccess() {
        String script = "secreto OP_SHA256 SHA256_secreto OP_EQUAL OP_IF OP_1 OP_ELSE OP_0 OP_ENDIF";
        assertTrue(run(script));
    }

    @Test
    @DisplayName("HTLC simplificado con preimagen incorrecta resulta inválido")
    void testHtlcStyleFail() {
        String script = "dato_falso OP_SHA256 SHA256_secreto OP_EQUAL OP_IF OP_1 OP_ELSE OP_0 OP_ENDIF";
        assertFalse(run(script));
    }

    @Test
    @DisplayName("OP_CHECKMULTISIG acepta 2 firmas válidas de 3")
    void testCheckMultiSigSuccess() {
        String script = "OP_0 SIG_Alice SIG_Bob OP_2 Alice Bob Carlos OP_3 OP_CHECKMULTISIG";
        assertTrue(run(script));
    }

    @Test
    @DisplayName("OP_CHECKMULTISIG rechaza firmas insuficientes o inválidas")
    void testCheckMultiSigFail() {
        String script = "OP_0 SIG_Alice SIG_impostor OP_2 Alice Bob Carlos OP_3 OP_CHECKMULTISIG";
        assertFalse(run(script));
    }

    @Test
    @DisplayName("OP_CHECKMULTISIGVERIFY pasa con 2 firmas válidas de 3")
    void testCheckMultiSigVerifySuccess() {
        String script = "OP_0 SIG_Alice SIG_Carlos OP_2 Alice Bob Carlos OP_3 OP_CHECKMULTISIGVERIFY OP_1";
        assertTrue(run(script));
    }

    @Test
    @DisplayName("OP_CHECKMULTISIGVERIFY falla cuando no se alcanzan las firmas requeridas")
    void testCheckMultiSigVerifyFail() {
        String script = "OP_0 SIG_Alice SIG_impostor OP_2 Alice Bob Carlos OP_3 OP_CHECKMULTISIGVERIFY";
        RuntimeException ex = runFail(script);
        assertTrue(ex.getMessage().contains("OP_CHECKMULTISIGVERIFY"));
    }

    @Test
    @DisplayName("Pila vacía provoca error al usar OP_DUP")
    void testEmptyStackError() {
        RuntimeException ex = runFail("OP_DUP");
        assertTrue(ex.getMessage().toLowerCase().contains("pila vacia"));
    }

    @Test
    @DisplayName("Tipo incorrecto provoca error en opcode aritmético")
    void testWrongTypeInArithmetic() {
        assertThrows(NumberFormatException.class, () -> new Interpreter(false).execute("hola 3 OP_ADD"));
    }

    @Test
    @DisplayName("Input nulo produce excepción")
    void testNullInput() {
        assertThrows(NullPointerException.class, () -> new Interpreter(false).execute(null));
    }
}
