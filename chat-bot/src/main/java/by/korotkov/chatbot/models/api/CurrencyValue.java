package by.korotkov.chatbot.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyValue {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private String price;

    @Override
    public String toString() {
        return "Криптовалюта: " + symbol + '\n' +
                "Курс: " + price;
    }
}
