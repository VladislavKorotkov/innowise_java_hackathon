package by.korotkov.chatbot.services;

import by.korotkov.chatbot.api.HttpService;
import by.korotkov.chatbot.models.Percent;
import by.korotkov.chatbot.models.UserChatBot;
import by.korotkov.chatbot.models.ValueCurrency;
import by.korotkov.chatbot.models.api.CurrencyValue;
import by.korotkov.chatbot.utils.CurrenciesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DecimalFormat;
import java.util.*;

@Component
@Slf4j
public class TelegramMessageSender {
    private final TelegramBot telegramBot;
    private final HttpService httpService;
    private final UserService userService;
    private final CurrencyService currencyService;
    public TelegramMessageSender(TelegramBot telegramBot, HttpService httpService, UserService userService, CurrencyService currencyService) {
        this.telegramBot = telegramBot;
        this.httpService = httpService;
        this.userService = userService;
        this.currencyService = currencyService;
    }

    public void sendMessages() {
        Map<String, CurrencyValue> currencies = httpService.fetchData();
        List<UserChatBot> users = userService.getUsers();
        for (UserChatBot userChatBot : users) {
            StringBuilder stringBuilder = new StringBuilder("ИЗМЕНЕНИЯ КУРСОВ ВАЛЮТ:\n");
            boolean flag =false;
            List<ValueCurrency> valueCurrencies = userChatBot.getValueCurrencies();
            for (ValueCurrency valueCurrency : valueCurrencies) {
                String symbolValue = valueCurrency.getSymbol().getSymbol();
                CurrencyValue currencyValue = currencies.get(symbolValue);
                if (currencyValue != null && symbolValue.equals(currencyValue.getSymbol())) {
                    Double value = valueCurrency.getValue();
                    Double newValue = Double.valueOf(currencyValue.getPrice());
                    int percent = userChatBot.getPercent().getIntValue();
                    double fivePercent = value * ((double) percent /100);
                    if (newValue > value + fivePercent || newValue < value - fivePercent) {
                        currencyService.setCourseCurrency(userChatBot.getUserId(), valueCurrency.getSymbol(), newValue);
                        stringBuilder.append("\n").append(currencyValue.toString());
                        double percentChange = ((newValue - value)/ value) * 100;
                        String formattedDouble = new DecimalFormat("#0.00").format(percentChange);
                        stringBuilder.append("\nИзменение: ").append(formattedDouble).append(" %\n");
                        flag = true;
                    }
                }
            }
            if(flag){
                SendMessage message = new SendMessage();
                message.setText(stringBuilder.toString());
                message.setChatId(userChatBot.getUserId());
                try {
                    telegramBot.execute(message);
                } catch (TelegramApiException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }
}