import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuctionSystemSimpleTest {
    private AuctionSystem system;
    private TestClient a;
    private TestClient b;

    @BeforeEach
    void setUp() {
        system = new AuctionSystem();
        a = new TestClient("a");
        b = new TestClient("b");
    }

    @Test
    void initial() {
        assertAll(
                () -> assertNull(system.getHighestBid()),
                () -> assertNull(system.getAuctionLeader()),
                () -> assertEquals(AuctionStatus.INPROGRESS, system.getAuctionStatus()),
                () -> assertTrue(system.getRegisteredClients().isEmpty()),
                () -> assertTrue(system.getActiveBidders().isEmpty()),
                () -> assertTrue(system.getHighestBids().isEmpty()));
    }

    @Test
    void register() {
        register(a, b);
        system.makeOffer(offer(a, value(100)));
        assertEquals(Set.of(a, b), system.getRegisteredClients());
        assertEquals(Set.of(a, b), system.getActiveBidders());
        assertEquals(AuctionStatus.INPROGRESS, system.getAuctionStatus());
    }

    @Test
    void joinAfterOffer() {
        register(a, b);
        TestValue accepted = value(100);
        system.makeOffer(offer(a, accepted));
        TestClient c = new TestClient("c");
        system.registerClient(c);

        assertEquals(Set.of(a, b, c), system.getRegisteredClients());
        assertEquals(Set.of(a, b, c), system.getActiveBidders());
        assertSame(accepted, system.getHighestBid());
        assertFalse(system.getHighestBids().containsKey(c));
        assertEquals(AuctionStatus.INPROGRESS, system.getAuctionStatus());
    }

    @Test
    void unregisteredOffer() {
        register(a, b);
        TestValue accepted = value(100);
        system.makeOffer(offer(a, accepted));
        system.makeOffer(offer(new TestClient("x"), value(1000)));
        assertSame(accepted, system.getHighestBid());
        assertSame(a, system.getAuctionLeader());
        assertEquals(Set.of(a), system.getHighestBids().keySet());
        assertEquals(AuctionStatus.INPROGRESS, system.getAuctionStatus());
    }

    @Test
    void firstOffer() {
        register(a, b);
        TestValue bid = value(0);
        system.makeOffer(offer(a, bid));
        assertSame(bid, system.getHighestBid());
        assertSame(a, system.getAuctionLeader());
        assertSame(bid, system.getHighestBids().get(a));
    }

    @Test
    void higherOffer() {
        register(a, b);
        TestValue low = value(100);
        TestValue high = value(200);
        system.makeOffer(offer(a, low));
        system.makeOffer(offer(b, high));
        assertSame(high, system.getHighestBid());
        assertSame(b, system.getAuctionLeader());
    }

    @Test
    void lowerOffer() {
        register(a, b);
        TestValue high = value(200);
        TestValue low = value(100);
        system.makeOffer(offer(a, high));
        system.makeOffer(offer(b, low));
        assertSame(high, system.getHighestBid());
        assertSame(a, system.getAuctionLeader());
        assertSame(low, system.getHighestBids().get(b));
    }

    @Test
    void equalOffer() {
        register(a, b);
        TestValue first = value(100);
        TestValue equal = value(100);
        system.makeOffer(offer(a, first));
        system.makeOffer(offer(b, equal));
        assertSame(first, system.getHighestBid());
        assertSame(a, system.getAuctionLeader());
        assertSame(equal, system.getHighestBids().get(b));
    }

    @Test
    void lowerOwnOffer() {
        register(a, b);
        TestValue high = value(200);
        system.makeOffer(offer(a, high));
        system.makeOffer(offer(a, value(100)));
        assertSame(high, system.getHighestBids().get(a));
    }

    @Test
    void equalOwnOffer() {
        register(a, b);
        TestValue first = value(100);
        system.makeOffer(offer(a, first));
        system.makeOffer(offer(a, value(100)));
        assertSame(first, system.getHighestBids().get(a));
    }

    @Test
    void higherOwnOffer() {
        register(a, b);
        TestValue high = value(200);
        system.makeOffer(offer(a, value(100)));
        system.makeOffer(offer(a, high));
        assertSame(high, system.getHighestBids().get(a));
        assertSame(high, system.getHighestBid());
    }

    @Test
    void withdraw() {
        register(a, b);
        system.makeOffer(offer(a, value(100)));
        system.makeOffer(offer(b, value(50)));
        system.makeOffer(offer(b, null));
        assertEquals(Set.of(a), system.getActiveBidders());
        assertEquals(2, system.getHighestBids().size());
        assertSame(a, system.getAuctionLeader());
        assertEquals(AuctionStatus.FINISHED, system.getAuctionStatus());
    }

    @Test
    void leaderWithdraw() {
        register(a, b);
        TestValue high = value(100);
        system.makeOffer(offer(a, high));
        system.makeOffer(offer(b, value(50)));
        system.makeOffer(offer(a, null));
        assertSame(high, system.getHighestBid());
        assertSame(a, system.getAuctionLeader());
        assertEquals(AuctionStatus.INPROGRESS, system.getAuctionStatus());
    }

    @Test
    void offerAfterWithdraw() {
        register(a, b);
        TestValue first = value(100);
        system.makeOffer(offer(a, first));
        system.makeOffer(offer(b, value(50)));
        system.makeOffer(offer(a, null));
        system.makeOffer(offer(a, value(1000)));
        assertSame(first, system.getHighestBid());
        assertSame(first, system.getHighestBids().get(a));
    }

    @Test
    void offerAfterFinish() {
        register(a, b);
        TestValue first = value(100);
        system.makeOffer(offer(a, first));
        system.makeOffer(offer(b, value(50)));
        system.makeOffer(offer(b, null));
        assertEquals(AuctionStatus.FINISHED, system.getAuctionStatus());

        system.makeOffer(offer(a, value(200)));

        assertSame(first, system.getHighestBid());
        assertSame(first, system.getHighestBids().get(a));
        assertSame(a, system.getAuctionLeader());
        assertEquals(Set.of(a), system.getActiveBidders());
        assertEquals(AuctionStatus.FINISHED, system.getAuctionStatus());
    }

    @Test
    void collectionCopy() {
        register(a, b);
        system.makeOffer(offer(a, value(100)));
        clear(system.getRegisteredClients());
        clear(system.getActiveBidders());
        clear(system.getHighestBids());
        assertEquals(Set.of(a, b), system.getRegisteredClients());
        assertEquals(Set.of(a, b), system.getActiveBidders());
        assertEquals(Set.of(a), system.getHighestBids().keySet());
    }

    static TestValue value(int value) {
        return new TestValue(value);
    }

    static AuctionOffer offer(Client client, OfferValue value) {
        return new AuctionOffer(client, new TestOffer(value));
    }

    private void register(Client... clients) {
        for (Client client : clients) system.registerClient(client);
    }

    private static void clear(Set<?> set) {
        try {
            set.clear();
        } catch (UnsupportedOperationException ignored) {
        }
    }

    private static void clear(Map<?, ?> map) {
        try {
            map.clear();
        } catch (UnsupportedOperationException ignored) {
        }
    }
}
