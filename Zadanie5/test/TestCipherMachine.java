import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class TestCipherMachine implements CipherMachineInterface {

    private final String eMess;
    private final Map<Character, Character> cipher;
    private final Map<Character, Integer> checks = new HashMap<>();
    private final List<Character> cEChar = new ArrayList<>();

    TestCipherMachine(String eMess, Map<Character, Character> cipher) {
        this.eMess = eMess;
        this.cipher = new HashMap<>(cipher);
    }

    @Override
    public String getEncryptedMessage() {
        return eMess;
    }

    @Override
    public void checkCharacter(char eChar, char pChar)
            throws TooLowException, TooHighException {
        if (eMess.indexOf(eChar) < 0) {
            throw new AssertionError("Sprawdzono znak spoza wiadomosci: " + eChar);
        }
        checks.merge(eChar, 1, Integer::sum);
        cEChar.add(eChar);
        Character exp = cipher.get(eChar);
        if (exp == null) {
            throw new AssertionError("Sprawdzono nieoczekiwany zaszyfrowany znak: "
                    + eChar);
        }
        int comp = Integer.compare(
                CipherDecoderInterface.ALPHABET.indexOf(pChar),
                CipherDecoderInterface.ALPHABET.indexOf(exp));
        if (comp < 0) {
            throw new TooLowException(eChar, pChar);
        }
        if (comp > 0) {
            throw new TooHighException(eChar, pChar);
        }
    }

    int checksFor(char eChar) {
        return checks.getOrDefault(eChar, 0);
    }

    int totalChecks() {
        return cEChar.size();
    }

    List<Character> checkedEncryptedCharacters() {
        return Collections.unmodifiableList(cEChar);
    }
}
