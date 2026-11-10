final class TestMemoryCard implements MemoryCardInterface {

    private final char[] characters;
    private int p;
    private int rC;
    private boolean ex;

    TestMemoryCard(String contents) {
        characters = contents.toCharArray();
    }

    @Override
    public Character readNextCharacter() {
        if (ex) {
            return null;
        }
        rC++;
        if (p == characters.length) {
            ex = true;
            return null;
        }
        return characters[p++];
    }

    int readCount() {
        return rC;
    }
}
