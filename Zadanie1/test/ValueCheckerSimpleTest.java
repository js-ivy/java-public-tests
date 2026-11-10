import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValueCheckerSimpleTest {
    private ValueChecker checker;

    @BeforeEach
    void setUp() {
        checker = new ValueChecker();
    }

    @Test void initial() { assertEquals(0, checker.output()); }

    @Test void even() {
        checker.input(8);
        assertEquals(8, checker.output());
    }

    @Test void odd() {
        checker.input(7);
        assertEquals(7, checker.output());
    }

    @Test void max() {
        checker.input(1000);
        assertEquals(1000, checker.output());
    }

    @Test void maxAfterOdd() {
        checker.input(1);
        checker.input(1000);
        assertEquals(501, checker.output());
    }

    @Test void zero() {
        checker.input(0);
        assertEquals(0, checker.output());
    }

    @Test void ignoredAfterStop() {
        checker.input(0);
        checker.input(10);
        assertEquals(0, checker.output());
    }

    @Test void continueAfterStop() {
        checker.input(0);
        checker.continueProcessing();
        checker.input(10);
        assertEquals(10, checker.output());
    }

    @Test void continueWhenActive() {
        checker.input(3);
        checker.continueProcessing();
        checker.input(4);
        assertEquals(5, checker.output());
    }

    @Test void reset() {
        checker.input(9);
        checker.reset();
        assertEquals(0, checker.output());
    }

    @Test void resetAfterStop() {
        checker.input(0);
        checker.reset();
        checker.input(4);
        assertEquals(4, checker.output());
    }

    @Test void repeatedOutput() {
        checker.input(6);
        assertEquals(6, checker.output());
        assertEquals(6, checker.output());
    }
}
