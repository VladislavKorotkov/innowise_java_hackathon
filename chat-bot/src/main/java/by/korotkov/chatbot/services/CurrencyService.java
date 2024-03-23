package by.korotkov.chatbot.services;

import by.korotkov.chatbot.api.HttpService;
import by.korotkov.chatbot.models.Symbol;
import by.korotkov.chatbot.models.api.CurrencyValue;
import by.korotkov.chatbot.repositories.CurrencyRepository;
import by.korotkov.chatbot.utils.CurrenciesMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyService {
    private final HttpService httpService;
    private final CurrencyRepository currencyRepository;

    public CurrencyService(HttpService httpService, CurrencyRepository currencyRepository) {
        this.httpService = httpService;
        this.currencyRepository = currencyRepository;
    }

    public String getCurrentCourses(){
        return CurrenciesMapper.convertMapToString(httpService.fetchData());
    }

    public void setCourseCurrency(Long userId, Symbol symbol){
        Map<String, CurrencyValue> currencies = httpService.fetchData();
        CurrencyValue currencyValue = currencies.get(symbol.getSymbol());
        currencyRepository.addOrUpdateCurrency(userId, symbol, Double.valueOf(currencyValue.getPrice()));
    }

    public void setCourseCurrency(Long userId, Symbol symbol, Double currencyValue){
        currencyRepository.addOrUpdateCurrency(userId, symbol, currencyValue);
    }
}
