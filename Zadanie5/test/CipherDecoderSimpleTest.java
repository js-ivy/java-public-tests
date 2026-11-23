import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class CipherDecoderSimpleTest {
    @Test void initial() { assertTrue(new CipherDecoder().getCipher().isEmpty()); }

    @Test void specialCharacters() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        TestCipherMachine machine = new TestCipherMachine(" ,.-!?_\n", Map.of());
        assertEquals(" ,.-!?_\n", decoder.decode(machine));
        assertEquals(0, machine.totalChecks());
        assertTrue(decoder.getCipher().isEmpty());
    }

    @Test void alphabetBoundaries() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        TestCipherMachine machine = new TestCipherMachine("AB", Map.of('A', '0', 'B', 'Z'));
        assertEquals("0Z", decoder.decode(machine));
    }

    @Test void repeatedCharacter() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        TestCipherMachine machine = new TestCipherMachine("AAAA", Map.of('A', 'M'));
        assertEquals("MMMM", decoder.decode(machine));
    }

    @Test void rememberedMapping() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        decoder.decode(new TestCipherMachine("A", Map.of('A', 'M')));
        TestCipherMachine next = new TestCipherMachine("A-A-A", Map.of('A', 'M'));
        assertEquals("M-M-M", decoder.decode(next));
        assertEquals(1, next.checksFor('A'));
    }

    @Test void overlappingExtension() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        decoder.decode(new TestCipherMachine("A", Map.of('A', 'M')));

        assertEquals("M7", decoder.decode(
                new TestCipherMachine("AB", Map.of('A', 'M', 'B', '7'))));
        assertEquals(Map.of('A', 'M', 'B', '7'), decoder.getCipher());
    }

    @Test void separateExtension() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        decoder.decode(new TestCipherMachine("A", Map.of('A', 'M')));

        assertEquals("K", decoder.decode(new TestCipherMachine("7", Map.of('7', 'K'))));
        assertEquals(Map.of('A', 'M', '7', 'K'), decoder.getCipher());
    }

    @Test void conflictingCipher() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        decoder.decode(new TestCipherMachine("A", Map.of('A', 'M')));

        assertThrows(DecodingException.class, () -> decoder.decode(
                new TestCipherMachine("BA", Map.of('B', '7', 'A', 'N'))));

        assertEquals(Map.of('A', 'M'), decoder.getCipher());
    }

    @Test void emptyMessage() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        TestCipherMachine machine = new TestCipherMachine("", Map.of());
        assertEquals("", decoder.decode(machine));
        assertEquals(0, machine.totalChecks());
        assertTrue(decoder.getCipher().isEmpty());
    }

    @Test void cipherCopy() throws Exception {
        CipherDecoder decoder = new CipherDecoder();
        decoder.decode(new TestCipherMachine("A", Map.of('A', '3')));
        Map<Character, Character> copy = decoder.getCipher();
        copy.clear();
        assertEquals(Map.of('A', '3'), decoder.getCipher());
    }

    @Test void tooLowFailure() {
        CipherDecoder decoder = new CipherDecoder();
        DecodingException exception = assertThrows(DecodingException.class,
                () -> decoder.decode(alwaysTooLow("A")));
        TooLowException cause = assertInstanceOf(TooLowException.class, exception.getCause());
        assertEquals('A', cause.getEncryptedCharacter());
        assertEquals('Z', cause.getProposedCharacter());
        assertTrue(decoder.getCipher().isEmpty());
    }

    @Test void tooHighFailure() {
        CipherDecoder decoder = new CipherDecoder();
        DecodingException exception = assertThrows(DecodingException.class,
                () -> decoder.decode(alwaysTooHigh("A")));
        TooHighException cause = assertInstanceOf(TooHighException.class, exception.getCause());
        assertEquals('0', cause.getProposedCharacter());
        assertTrue(decoder.getCipher().isEmpty());
    }

    @Test void adjacentResponses() {
        CipherDecoder decoder = new CipherDecoder();
        DecodingException exception = assertThrows(DecodingException.class,
                () -> decoder.decode(adjacentResponses("X")));

        assertInstanceOf(CipherSearchException.class, exception.getCause());
        assertTrue(decoder.getCipher().isEmpty());
    }

    static CipherMachineInterface alwaysTooLow(String message) {
        return new CipherMachineInterface() {
            public String getEncryptedMessage() { return message; }
            public void checkCharacter(char encrypted, char proposed) throws TooLowException {
                throw new TooLowException(encrypted, proposed);
            }
        };
    }

    static CipherMachineInterface alwaysTooHigh(String message) {
        return new CipherMachineInterface() {
            public String getEncryptedMessage() { return message; }
            public void checkCharacter(char encrypted, char proposed) throws TooHighException {
                throw new TooHighException(encrypted, proposed);
            }
        };
    }

    static CipherMachineInterface adjacentResponses(String message) {
        return new CipherMachineInterface() {
            public String getEncryptedMessage() { return message; }
            public void checkCharacter(char encrypted, char proposed)
                    throws TooLowException, TooHighException {
                if (CipherDecoderInterface.ALPHABET.indexOf(proposed)
                        <= CipherDecoderInterface.ALPHABET.indexOf('A')) {
                    throw new TooLowException(encrypted, proposed);
                }
                throw new TooHighException(encrypted, proposed);
            }
        };
    }
}
