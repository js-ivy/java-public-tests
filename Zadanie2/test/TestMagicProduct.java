final class TestMagicProduct implements MagicProductInterface {
    private final DataType eT;
    private Integer iV;
    private Double dV;
    private Character cV;
    private String sV;

    TestMagicProduct(DataType eT, Integer iV, Double dV,
                     Character cV, String sV) {
        this.eT = eT;
        this.iV = iV;
        this.dV = dV;
        this.cV = cV;
        this.sV = sV;
    }

    @Override public DataType getExpectedType() { return eT; }
    @Override public Integer getIntegerValue() { return iV; }
    @Override public void setIntegerValue(Integer val) { iV = val; }
    @Override public Double getDoubleValue() { return dV; }
    @Override public void setDoubleValue(Double val) { dV = val; }
    @Override public Character getCharacterValue() { return cV; }
    @Override public void setCharacterValue(Character val) { cV = val; }
    @Override public String getStringValue() { return sV; }
    @Override public void setStringValue(String val) { sV = val; }
}
