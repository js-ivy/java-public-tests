import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MemoryPairFinderSimpleTest {
    @Test void initial() { assertTrue(new MemoryPairFinder().getPairs().isEmpty()); }

    @Test void sameOrder() { assertMatch("abc", "abc"); }
    @Test void differentOrder() { assertMatch("aabc", "caba"); }
    @Test void empty() { assertMatch("", ""); }
    @Test void digits() { assertMatch("1123", "3211"); }
    @Test void alphanumeric() { assertMatch("a1b2", "2ba1"); }
    @Test void caseSensitive() { assertNoMatch("a", "A"); }
    @Test void duplicateSensitive() { assertNoMatch("aab", "abb"); }

    @Test void singleCard() {
        MemoryPairFinder f = new MemoryPairFinder();
        f.process(new TestMemoryCard("a"));
        assertTrue(f.getPairs().isEmpty());
    }

    @Test void fullRead() {
        MemoryPairFinder f = new MemoryPairFinder();
        TestMemoryCard card = new TestMemoryCard("abc");
        f.process(card);
        assertEquals(4, card.readCount());
    }

    @Test void mapCopy() {
        MemoryPairFinder f = new MemoryPairFinder();
        TestMemoryCard a = new TestMemoryCard("x");
        TestMemoryCard b = new TestMemoryCard("x");
        f.process(a);
        f.process(b);
        f.getPairs().clear();
        assertSame(b, f.getPairs().get(a));
    }

    static void assertMatch(String left, String right) {
        MemoryPairFinder f = new MemoryPairFinder();
        TestMemoryCard a = new TestMemoryCard(left);
        TestMemoryCard b = new TestMemoryCard(right);
        f.process(a);
        f.process(b);
        Map<MemoryCardInterface, MemoryCardInterface> pairs = f.getPairs();
        assertEquals(1, pairs.size());
        assertSame(b, pairs.get(a));
    }

    private static void assertNoMatch(String left, String right) {
        MemoryPairFinder f = new MemoryPairFinder();
        f.process(new TestMemoryCard(left));
        f.process(new TestMemoryCard(right));
        assertTrue(f.getPairs().isEmpty());
    }
}
