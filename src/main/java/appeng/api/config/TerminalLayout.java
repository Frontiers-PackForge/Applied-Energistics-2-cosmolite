package appeng.api.config;

public enum TerminalLayout {
    COLUMNS_9(9),
    COLUMNS_11(11),
    COLUMNS_13(13),
    COLUMNS_15(15);

    private final int columns;

    TerminalLayout(int columns) {
        this.columns = columns;
    }

    public int getColumns() {
        return this.columns;
    }
}
