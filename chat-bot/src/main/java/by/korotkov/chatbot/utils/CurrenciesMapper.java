package by.korotkov.chatbot.utils;

import by.korotkov.chatbot.models.Symbol;
import by.korotkov.chatbot.models.api.CurrencyValue;

import java.util.HashMap;
import java.util.Map;

public class CurrenciesMapper {
    public static Map<String, CurrencyValue> convertArrayToMap(CurrencyValue[] currencies){
        Map<String, CurrencyValue> currencyMap = new HashMap<>();
        for (CurrencyValue currency : currencies) {
            try {
                Symbol symbol = Symbol.valueOf(currency.getSymbol());
                currencyMap.put(symbol.getSymbol(), currency);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return currencyMap;
    }

    public static String convertMapToString(Map<String, CurrencyValue> currencies){

        StringBuilder sb = new StringBuilder();
        sb.append("КУРСЫ КРИПТОВАЛЮТ:\n\n");

        for (Map.Entry<String, CurrencyValue> entry : currencies.entrySet()) {
            String symbol = entry.getKey();
            CurrencyValue currency = entry.getValue();

            sb.append("Криптовалюта: ").append(symbol).append("\n");
            sb.append("Курс: ").append(currency.getPrice()).append("\n\n");
        }

        return sb.toString();
    }
}
