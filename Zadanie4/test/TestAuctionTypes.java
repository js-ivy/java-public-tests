record TestClient(String id) implements Client {}

final class TestValue implements OfferValue {
    private final int val;

    TestValue(int v) {
        this.val = v;
    }

    @Override
    public int compareTo(OfferValue other) {
        return Integer.compare(val, ((TestValue) other).val);
    }
}

final class TestOffer implements Offer {
    private final OfferValue val;

    TestOffer(OfferValue v) {
        this.val = v;
    }

    @Override
    public OfferValue getOfferValue() {
        return val;
    }
}
