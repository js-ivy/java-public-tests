import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class MachineControllerSimpleTest {
    @Test void initial() {
        MachineController c = new MachineController();
        assertTrue(c.getValidProducts().isEmpty());
        assertTrue(c.getEmptyProducts().isEmpty());
        assertData(c.getReturnData(), List.of(), List.of(), List.of(), List.of());
    }

    @Test void validInteger() { assertValid(product(DataType.INTEGER, 1, null, null, null)); }
    @Test void validDouble() { assertValid(product(DataType.DOUBLE, null, 1.5, null, null)); }
    @Test void validCharacter() { assertValid(product(DataType.CHARACTER, null, null, 'a', null)); }
    @Test void validString() { assertValid(product(DataType.STRING, null, null, null, "a")); }

    @Test void emptyInteger() { assertEmpty(product(DataType.INTEGER, null, null, null, null)); }
    @Test void emptyDouble() { assertEmpty(product(DataType.DOUBLE, null, null, null, null)); }
    @Test void emptyCharacter() { assertEmpty(product(DataType.CHARACTER, null, null, null, null)); }
    @Test void emptyString() { assertEmpty(product(DataType.STRING, null, null, null, null)); }

    @Test void repairValid() {
        MachineController c = new MachineController();
        TestMagicProduct p = product(DataType.INTEGER, 1, 2.0, 'x', "y");
        c.process(p);
        assertValues(p, 1, null, null, null);
        assertSame(p, c.getValidProducts().get(0));
        assertData(c.getReturnData(), List.of(), List.of(2.0), List.of('x'), List.of("y"));
    }

    @Test void repairEmpty() {
        MachineController c = new MachineController();
        TestMagicProduct p = product(DataType.STRING, 1, 2.0, 'x', null);
        c.process(p);
        assertValues(p, null, null, null, null);
        assertSame(p, c.getEmptyProducts().get(0));
    }

    @Test void returnCleared() {
        MachineController c = new MachineController();
        c.process(product(DataType.INTEGER, 1, 2.0, null, null));
        c.getReturnData();
        assertData(c.getReturnData(), List.of(), List.of(), List.of(), List.of());
    }

    @Test void listCopy() {
        MachineController c = new MachineController();
        TestMagicProduct p = product(DataType.INTEGER, 1, null, null, null);
        c.process(p);
        c.getValidProducts().clear();
        assertEquals(List.of(p), c.getValidProducts());
    }

    static TestMagicProduct product(DataType type, Integer i, Double d, Character ch, String s) {
        return new TestMagicProduct(type, i, d, ch, s);
    }

    static void assertValues(TestMagicProduct p, Integer i, Double d, Character ch, String s) {
        assertAll(() -> assertEquals(i, p.getIntegerValue()), () -> assertEquals(d, p.getDoubleValue()),
                () -> assertEquals(ch, p.getCharacterValue()), () -> assertEquals(s, p.getStringValue()));
    }

    static void assertData(ReturnData data, List<Integer> i, List<Double> d,
                           List<Character> ch, List<String> s) {
        assertAll(() -> assertEquals(i, data.integers()), () -> assertEquals(d, data.doubles()),
                () -> assertEquals(ch, data.characters()), () -> assertEquals(s, data.strings()));
    }

    private static void assertValid(TestMagicProduct p) {
        MachineController c = new MachineController();
        c.process(p);
        assertEquals(List.of(p), c.getValidProducts());
        assertTrue(c.getEmptyProducts().isEmpty());
    }

    private static void assertEmpty(TestMagicProduct p) {
        MachineController c = new MachineController();
        c.process(p);
        assertEquals(List.of(p), c.getEmptyProducts());
        assertTrue(c.getValidProducts().isEmpty());
    }
}
