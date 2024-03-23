package by.korotkov.chatbot.models;

public enum Symbol {
    HEXUSDT("HEXUSDT"),
    HIGHUSDT("HIGHUSDT"),
    SHIAUSDT("SHIAUSDT"),
    APXUSDT("APXUSDT"),
    MIDDLEUSDT("MIDDLEUSDT"),
    XRPBABYUSDT("XRPBABYUSDT"),
    TOMBPLUSUSDT("TOMBPLUSUSDT"),
    INJUSDT("INJUSDT"),
    MINTIUSDT("MINTIUSDT"),
    FNZUSDT("FNZUSDT");

    private final String symbol;

    Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Symbol fromString(String symbol) {
        for (Symbol s : Symbol.values()) {
            if (s.symbol.equalsIgnoreCase(symbol)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid symbol: " + symbol);
    }
}
