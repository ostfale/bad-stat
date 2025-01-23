package de.ostfale.qk.app;

public enum DirTypes {
    CONFIG("config"),
    DATA("data"),
    LOG("logs");

    public final String displayName;

    DirTypes(String aName) {
        this.displayName = aName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
