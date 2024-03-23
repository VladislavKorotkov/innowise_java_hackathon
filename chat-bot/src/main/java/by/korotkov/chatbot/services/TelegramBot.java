package by.korotkov.chatbot.services;
import by.korotkov.chatbot.config.BotConfig;
import by.korotkov.chatbot.models.Percent;
import by.korotkov.chatbot.models.Symbol;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private  final BotConfig botConfig;
    private final UserService userService;
    private final CurrencyService currencyService;
    public TelegramBot(BotConfig botConfig, UserService userService, CurrencyService currencyService){
        this.botConfig = botConfig;
        this.userService = userService;
        this.currencyService = currencyService;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Добрый вечер"));
        listOfCommands.add(new BotCommand("/percents", "Установка процента изменения криптовалют"));
        listOfCommands.add(new BotCommand("/current_courses", "Текущий курс"));
        listOfCommands.add(new BotCommand("/select_currency", "Выбор отслеживаемых криптовалют"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }

    }
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {

                case "/start" -> {
                    sendMessage("Данный бот предназначен для получения актуальных значений криптовалют", chatId);
                }
                case "/percents" -> {
                    sendButtonPanel(chatId);
                }
                case "/current_courses" ->{
                    sendMessage(currencyService.getCurrentCourses(), chatId);
                }
                case "/select_currency" ->{
                    sendCurrencyMessage(chatId);
                }
                default -> {
                    handleButtonSelection(chatId, messageText);
                }
            }
        }
    }

    private void sendButtonPanel(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите процент:");
        message.setReplyMarkup(getButtonPanelMarkup());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private ReplyKeyboardMarkup getButtonPanelMarkup() {
        ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup();
        replyMarkup.setOneTimeKeyboard(true);
        replyMarkup.setResizeKeyboard(true);
        List<Percent> percentValues = Arrays.asList(Percent.values());
        int buttonsPerRow = 2;
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow currentRow = new KeyboardRow();
        for (int i = 0; i < percentValues.size(); i++) {
            Percent percent = percentValues.get(i);
            String percentValue = String.valueOf(percent.getValue());
            KeyboardButton button = new KeyboardButton(percentValue);
            currentRow.add(button);
            if (currentRow.size() >= buttonsPerRow || i == percentValues.size() - 1) {
                rows.add(currentRow);
                currentRow = new KeyboardRow();
            }
        }
        replyMarkup.setKeyboard(rows);

        return replyMarkup;
    }

    private void handleButtonSelection(long chatId, String selectedValue) {
        if (selectedValue.endsWith("%")) {
            String percentValue = selectedValue.replace("%", "");
            try {
                Percent selectedPercent = null;
                for (Percent percent : Percent.values()) {
                    if (percent.getValue().equalsIgnoreCase(selectedValue)) {
                        selectedPercent = percent;
                        break;
                    }
                }
                if (selectedPercent != null) {
                    userService.addPercent(chatId, selectedPercent);
                    sendMessage("Процент изменения криптовалют выбран", chatId);
                } else {
                    sendMessage("Некорректный процент: " + percentValue, chatId);
                }
            } catch (NumberFormatException e) {
                sendMessage("Некорректное значение процента: " + percentValue, chatId);
            }
        }
        else{
                Symbol symbol = getSymbolFromText(selectedValue);
                if (symbol != null) {
                    currencyService.setCourseCurrency(chatId, symbol);
                    sendMessage("Данная криптовалюта добавлена в отслеживаемый список", chatId);
                }
                else{
                    sendMessage("Нераспознанное сообщение", chatId);
                }
        }

    }

    public void sendCurrencyMessage(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите криптовалюту для отслеживания ее курса:");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (Symbol symbol : Symbol.values()) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(symbol.getSymbol()));
            keyboard.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }


    }

    private void sendMessage(String textToSend, long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        send(message);
    }
    private void send(SendMessage msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }
    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    private Symbol getSymbolFromText(String text) {
        for (Symbol symbol : Symbol.values()) {
            if (symbol.getSymbol().equalsIgnoreCase(text)) {
                return symbol;
            }
        }
        return null;
    }
}
