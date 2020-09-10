package util.common;



public enum Encoding {
    UTF_8("utf-8"),
    GBK("GBK");

    private String encoding;
    Encoding(String encoding){
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }
}
