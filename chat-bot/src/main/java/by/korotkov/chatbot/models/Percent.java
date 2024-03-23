package by.korotkov.chatbot.models;

public enum Percent {
    THREE("3%"),
    FIVE("5%"),
    TEN("10%"),
    FIFTEEN("15%");

    private final String value;

    Percent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getIntValue(){
        String percent = value.replaceAll("%", "");
        return Integer.parseInt(percent);
    }

    public static Percent fromString(String value) {
        for (Percent p : Percent.values()) {
            if (p.value.equalsIgnoreCase(value)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
